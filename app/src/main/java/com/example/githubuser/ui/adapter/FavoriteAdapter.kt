package com.example.githubuser.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.local.entity.FavoriteUserEntity
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.activity.DetailUserActivity

class FavoriteAdapter: ListAdapter<FavoriteUserEntity, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder (private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : FavoriteUserEntity){
            Glide.with(itemView.context)
                .load(item.avatarUrl)
                .into(binding.ivUser)
            binding.tvName.text = item.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listUser = getItem(position)
        holder.bind(listUser)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.USER, listUser.username)
            intent.putExtra(DetailUserActivity.KEY_ID,listUser.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUserEntity>(){
            override fun areItemsTheSame(oldItem: FavoriteUserEntity, newItem: FavoriteUserEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteUserEntity, newItem: FavoriteUserEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}