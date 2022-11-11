package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey

import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.databinding.ItemListUserBinding
import com.skripsi.perpustakaanapp.utils.GlideManagement
import com.skripsi.perpustakaanapp.utils.NetworkInfo.AVATAR_IMAGE_BASE_URL
import com.skripsi.perpustakaanapp.utils.NetworkInfo.BOOK_IMAGE_BASE_URL
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var glideManagement: GlideManagement
    private var listUser = mutableListOf<User>()
    var onItemClick: ((User) -> Unit)? = null

    fun setUserList(users: List<User>?) {
        if (users == null) return
        listUser.clear()
        listUser.addAll(users)
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
                tvFullName.text = user.firstName
                setAvatar(user.avatar, itemView)
            }
        }

        private fun setAvatar(avatar: String?, context: View) {
            if (avatar != null) {
                glideManagement = GlideManagement(context.context)
                Glide.with(context)
                    .load(AVATAR_IMAGE_BASE_URL + avatar)
                    // For reload image on glide from the same url
                    .signature(ObjectKey(glideManagement.fetchCacheAvatar().toString()))
                    // To show the original size of image
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .fitCenter()
                    .into(binding.ivAvatar)
            }
        }

        init {
            binding.root.setSingleClickListener {
                onItemClick?.invoke(listUser[adapterPosition])
            }
        }
    }
}