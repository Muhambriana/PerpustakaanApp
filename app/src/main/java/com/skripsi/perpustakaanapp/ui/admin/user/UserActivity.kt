package com.skripsi.perpustakaanapp.ui.admin.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skripsi.perpustakaanapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}