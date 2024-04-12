package com.example.githubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.model.UserResponse
import com.example.githubuser.databinding.FragmentFollowersBinding
import com.example.githubuser.ui.adapter.UserAdapter
import com.example.githubuser.ui.viewmodel.DetailUserViewModel

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private val adapter by lazy {
        UserAdapter {
        }
    }
    private val viewModel by activityViewModels<DetailUserViewModel>()
    private var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFollowers.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowersFragment.adapter
        }

        when (type) {
            FOLLOWERS -> {
                viewModel.followersResult.observe(viewLifecycleOwner, this::manageFollowersResult)
            }

            FOLLOWING -> {
                viewModel.followingResult.observe(
                    viewLifecycleOwner,
                    this::manageFollowersResult
                )
            }
        }

    }

    private fun manageFollowersResult(state: Result) {
        when (state) {
            is Result.Success<*> -> {
                adapter.setData(state.data as MutableList<UserResponse.Users>)
            }

            is Result.Error -> {
                Toast.makeText(
                    requireActivity(),
                    state.exception.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Result.Loading -> {
                binding.progressBar.isVisible = state.isLoading
            }
        }
    }

    companion object {
        const val FOLLOWERS = 100
        const val FOLLOWING = 101

        fun newInstance(type: Int) = FollowersFragment()
            .apply {
                this.type = type
            }
    }
}