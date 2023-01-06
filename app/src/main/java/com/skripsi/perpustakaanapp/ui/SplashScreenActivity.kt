package com.skripsi.perpustakaanapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.databinding.ActivitySplashScreenBinding
import com.skripsi.perpustakaanapp.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val top = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        binding.imgSplash.startAnimation(bottom)
        binding.tvSplash.startAnimation(top)

        Handler(mainLooper).postDelayed({
            startActivity(
                Intent(this,
                    LoginActivity::class.java)
            )
            finish()
        }, 3500)
    }
}