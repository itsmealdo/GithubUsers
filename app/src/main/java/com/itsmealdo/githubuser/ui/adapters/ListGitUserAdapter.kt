package com.itsmealdo.githubuser.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itsmealdo.githubuser.R
import com.itsmealdo.githubuser.databinding.ItemUserRowBinding
import com.itsmealdo.githubuser.data.model.UserDetailResponse

class ListGitUserAdapter: RecyclerView.Adapter<ListGitUserAdapter.UserViewHolder>() {

    inner class UserViewHolder (private val binding: ItemUserRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserDetailResponse) {
            Glide.with(itemView)
                .load(user.avatarUrl)
                .placeholder(R.drawable.githublogo)
                .into(binding.ivUser)
            binding.tvUsername.text = user.login
            binding.tvLink.text = user.htmlUrl

            binding.root.setOnClickListener {
                onItemClickListener?.let { it(user) }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<UserDetailResponse>() {
        override fun areItemsTheSame(oldItem: UserDetailResponse, newItem: UserDetailResponse): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: UserDetailResponse, newItem: UserDetailResponse): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener: ((UserDetailResponse) -> Unit)? = null

    fun setOnItemClickListener(listener: (UserDetailResponse) -> Unit) {
        onItemClickListener = listener
    }

}

