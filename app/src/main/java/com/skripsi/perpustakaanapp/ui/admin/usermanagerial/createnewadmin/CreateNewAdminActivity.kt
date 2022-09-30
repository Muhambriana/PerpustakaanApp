package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.createnewadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.databinding.ActivityCreateNewAdminBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class CreateNewAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNewAdminBinding
    private lateinit var viewModel: CreateNewAdminViewModel

    private val client = RetrofitClient

    private var gender: Int? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rbGender.setOnCheckedChangeListener { _, i ->
            gender =
                when(i){
                    R.id.rb_gender_male -> 1
                    R.id.rb_gender_female -> 0
                    else -> -1
                }
        }


        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            CreateNewAdminViewModel::class.java
        )

        binding.progressBar.visibility = View.INVISIBLE

        binding.buttonSave.setSingleClickListener {
            askAppointment()
        }
    }

    private fun askAppointment() {
        val username = binding.edUsername.text.toString()
        when {
            username.isEmpty() -> {
                binding.edUsername.error = "NISN Tidak Boleh Kosong"
                binding.edUsername.requestFocus()
            }
            binding.rbGender.checkedRadioButtonId.equals(-1) -> {
                AlertDialog.Builder(this@CreateNewAdminActivity)
                    .setTitle("Warning")
                    .setMessage("Pilih Gender Terlebih Dahulu")
                    .setPositiveButton("Tutup") { _, _ ->
                        // do nothing
                    }
                    .show()
            }
            else -> {
                postUserData()
            }
        }
    }

    private fun postUserData() {
        //pass data to view model
        gender?.let {
            viewModel.createNewAdmin(
                binding.edUsername.text.toString(),
                binding.edtPassword.text.toString(),
                binding.edtFirstName.text.toString(),
                binding.edtLastName.text.toString(),
                "admin",
                binding.edEmail.text.toString(),
                binding.edtTelNumber.text.toString(),
                binding.edtAddress.text.toString(),
                it
            )
        }

        viewModel.resourceCreateAdmin.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(this@CreateNewAdminActivity, R.drawable.icon_checked, "SUCCESS", "Berhasil Membuat Admin Baru")
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(this@CreateNewAdminActivity, R.drawable.icon_cancel, "ERROR", resource.message.toString())
                    }
                }
            }
        }
    }
}