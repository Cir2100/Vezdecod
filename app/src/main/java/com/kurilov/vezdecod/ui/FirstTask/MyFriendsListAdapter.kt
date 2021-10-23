package com.kurilov.vezdecod.ui.FirstTask

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kurilov.vezdecod.databinding.FragmentFriendsItemBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import android.os.Bundle





class MyFriendsListAdapter : RecyclerView.Adapter<MyFriendsListAdapter.ViewHolder>() {
    private var items : List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentFriendsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.friendNameTextView.text = item
    }

    fun updateItems(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(binding: FragmentFriendsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val friendNameTextView: TextView = binding.textviewNameFriend
    }

}