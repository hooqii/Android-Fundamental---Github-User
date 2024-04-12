package com.example.githubuser.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.model.UserResponse
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.adapter.UserAdapter
import com.example.githubuser.ui.factory.ViewModelFactory
import com.example.githubuser.ui.viewmodel.MainViewModel
import com.example.githubuser.utils.SettingPreferences
import com.example.githubuser.utils.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private val adapter by lazy {
        UserAdapter {
            Intent(this, DetailUserActivity::class.java).apply {
                putExtra(DetailUserActivity.USER, it.login)
                startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        val switchTheme = binding.switchTheme

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        with(binding) {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.getUser(query.toString())
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })
            btnFavorite.setOnClickListener {
                startActivity(Intent(this@MainActivity, FavoriteUserActivity::class.java))
            }
        }

        viewModel.userResult.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    adapter.setData(it.data as MutableList<UserResponse.Users>)
                }

                is Result.Error -> {
                    val errorMessage = it.exception.message.toString()
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.e("DetailFragment", "Error fetching user data: $errorMessage")
                }

                is Result.Loading -> {
                    binding.pbLoading.isVisible = it.isLoading
                }
            }
        }
        viewModel.getUser()
    }
}

