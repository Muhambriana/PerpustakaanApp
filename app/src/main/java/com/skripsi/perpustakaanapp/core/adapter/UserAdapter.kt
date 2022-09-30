package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.databinding.ItemListUserBinding
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var listUser = mutableListOf<User>()
    var onItemClick: ((User) -> Unit)? = null

    fun setUserList(users: List<User>?) {
        if (users == null) return
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListUserBinding.bind(itemView)
        fun bind(user: User){
            with(binding){
                tvBookTitle.text = user.firstName
            }
        }

        init {
            binding.root.setSingleClickListener {
                onItemClick?.invoke(listUser[adapterPosition])
            }
        }
    }
}