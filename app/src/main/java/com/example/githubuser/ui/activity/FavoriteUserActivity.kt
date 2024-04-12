package com.example.githubuser.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.ActivityFavoriteUserBinding
import com.example.githubuser.ui.adapter.FavoriteAdapter
import com.example.githubuser.ui.factory.FavoriteViewModelFactory
import com.example.githubuser.ui.viewmodel.FavoriteUserViewModel

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = FavoriteViewModelFactory.getInstance(application)
        val viewModel: FavoriteUserViewModel = ViewModelProvider(this,factory)[FavoriteUserViewModel::class.java]

        val adapter = FavoriteAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavorite.addItemDecoration(itemDecoration)

        viewModel.getAllFavorites().observe(this){list ->
            if (list != null){
                adapter.submitList(list)
                binding.rvFavorite.adapter = adapter
            }

            if (list.isEmpty()){
                Toast.makeText(
                    this,
                    "There are no favorite users",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}