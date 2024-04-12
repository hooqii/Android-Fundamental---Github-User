package com.example.githubuser.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import com.example.githubuser.R
import com.example.githubuser.data.Result
import com.example.githubuser.data.local.entity.FavoriteUserEntity
import com.example.githubuser.data.remote.model.DetailResponse
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.ui.adapter.DetailAdapter
import com.example.githubuser.ui.factory.DetailViewModelFactory
import com.example.githubuser.ui.fragment.FollowersFragment
import com.example.githubuser.ui.viewmodel.DetailUserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private var userFavorite = FavoriteUserEntity()
    private var receivedData: String? = null
    private var isClicked = false

    companion object {
        const val USER = "username"
        const val KEY_ID = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(USER) ?: ""

        receivedData = username

        val viewModel: DetailUserViewModel by viewModels {
            DetailViewModelFactory(application)
        }

        viewModel.detailUserResult.observe(this) { it ->
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as DetailResponse
                    binding.apply {
                        userFavorite.id = user.id
                        userFavorite.username = user.name
                        userFavorite.avatarUrl = user.avatarUrl
                        ivProfile.load(user.avatarUrl)
                        tvName.text = user.name
                        "@${user.login}".also { tvUserName.text = it }
                        "${user.followers} ${getString(R.string.followers)}".also { tvFollowers.text = it }
                        "${user.following} ${getString(R.string.following)}".also { tvFollowing.text = it }
                        adjustTextViewWidth(
                            tvFollowers,
                            "${user.followers} ${getString(R.string.followers)}"
                        )
                        adjustTextViewWidth(
                            tvFollowing,
                            "${user.following} ${getString(R.string.following)}"
                        )
                    }
                    userFavorite = FavoriteUserEntity(user.id, user.login, user.avatarUrl)
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.pbLoading.isVisible = it.isLoading
                }
            }
        }

        viewModel.getDetailUser(username)

        val fragments = mutableListOf<Fragment>(
            FollowersFragment.newInstance(FollowersFragment.FOLLOWERS),
            FollowersFragment.newInstance(FollowersFragment.FOLLOWING)
        )
        val titleFragment = mutableListOf(
            getString(R.string.followers),
            getString(R.string.following)
        )

        val adapter = DetailAdapter(this, fragments)
        binding.viewPage.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPage) { tab, position ->
            tab.text = titleFragment[position]
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewModel.getFollowers(username)

        viewModel.getFavoriteUserByUsername(receivedData.toString()).observe(this) { favUser ->
            if (favUser != null) {
                binding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabFavorite.context,
                        R.drawable.baseline_favorite_24
                    )
                )
                isClicked = true
            } else {
                binding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabFavorite.context,
                        R.drawable.baseline_favorite_border_24
                    )
                )
                isClicked = false
            }
        }

        binding.fabFavorite.setOnClickListener {
            favoriteClick(viewModel)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun favoriteClick(viewModel: DetailUserViewModel) {
        if (isClicked) {
            viewModel.delete(userFavorite)
            Toast.makeText(
                this@DetailUserActivity,
                getString(R.string.hapus_favorite, userFavorite.username.toString()),
                Toast.LENGTH_LONG
            ).show()
        } else {
            viewModel.insert(userFavorite)
            Toast.makeText(
                this@DetailUserActivity,
                getString(R.string.tambah_favorite, userFavorite.username.toString()),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun adjustTextViewWidth(textView: TextView, text: String) {
        val params = textView.layoutParams as ConstraintLayout.LayoutParams
        val textWidth = textView.paint.measureText(text).toInt()
        params.width = textWidth
        textView.layoutParams = params
    }
}
