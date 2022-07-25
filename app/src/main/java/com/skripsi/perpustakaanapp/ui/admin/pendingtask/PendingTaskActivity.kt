package com.skripsi.perpustakaanapp.ui.admin.pendingtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skripsi.perpustakaanapp.databinding.ActivityPendingTaskBinding

class PendingTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPendingTaskBinding
    private lateinit var viewModel: PendingTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}