package com.example.locationsharingapp1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.locationsharingapp1.data.User
import com.example.locationsharingapp1.databinding.ItemUserBinding

class UserAdapter(private var userList: List<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.binding.tvName.text = currentUser.displayName
        holder.binding.tvEmail.text = currentUser.email
        holder.binding.tvLocation.text = currentUser.location
    }

    override fun getItemCount(): Int = userList.size

    fun updateData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }
}