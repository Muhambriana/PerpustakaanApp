package com.skripsi.perpustakaanapp.ui.developer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.databinding.ActivityAboutBinding
import com.skripsi.perpustakaanapp.utils.NetworkInfo.AVATAR_IMAGE_BASE_URL

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "About Developer"
        binding.textViewEmailProfil.text = "@thisismuham"
        setPhoto()
    }

    private fun setPhoto() {
        sessionManager = SessionManager(this)
        val imageUrl = GlideUrl("${AVATAR_IMAGE_BASE_URL}a_developer_profile.jpg/${System.currentTimeMillis()}") { mapOf(Pair("Authorization", sessionManager.fetchAuthToken())) }
        Glide.with(this)
            .load(imageUrl)

            .override(Target.SIZE_ORIGINAL)
            .fitCenter()
            .into(binding.ivPhotoProfile)
    }
}