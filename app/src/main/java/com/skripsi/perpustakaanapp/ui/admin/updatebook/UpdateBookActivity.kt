package com.skripsi.perpustakaanapp.ui.admin.updatebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skripsi.perpustakaanapp.databinding.ActivityUpdateBookBinding

class UpdateBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}