
package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.databinding.ItemListUserBinding
import com.skripsi.perpustakaanapp.utils.NetworkInfo.AVATAR_IMAGE_BASE_URL
import com.skripsi.perpustakaanapp.utils.setSingleClickListener
import java.util.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var sessionManager: SessionManager

    private var listUser = mutableListOf<User>()
    var onItemClick: ((User) -> Unit)? = null

    fun setUserList(users: List<User>?) {
        if (users == null) return
        listUser.clear()
        listUser.addAll(users)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        sessionManager = SessionManager(recyclerView.context)
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
                setAvatar(user.avatar)
            }
        }

        private fun setAvatar(avatar: String?) {
            if (avatar == null || avatar == "null") {
                binding.ivAvatar.setImageResource(R.color.teal_200)
                return
            }
            glideSetup(avatar)
        }

        private fun glideSetup(imageName: String?) {
            val randomString = UUID.randomUUID().toString()
            val imageUrl = GlideUrl("${AVATAR_IMAGE_BASE_URL}$imageName/?$randomString") { mapOf(Pair("Authorization", sessionManager.fetchAuthToken())) }


            Glide.with(itemView.context)
                .load(imageUrl)
                // To show the original size of image
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .fitCenter()
                .into(binding.ivAvatar)
        }

        init {
            binding.root.setSingleClickListener {
                onItemClick?.invoke(listUser[adapterPosition])
            }
        }
    }
}