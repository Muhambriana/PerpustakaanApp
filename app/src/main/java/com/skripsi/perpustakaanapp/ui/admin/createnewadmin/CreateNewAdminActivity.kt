package com.skripsi.perpustakaanapp.ui.admin.createnewadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityCreateNewAdminBinding
import com.skripsi.perpustakaanapp.ui.register.RegisterViewModel

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


        viewModel = ViewModelProvider(this, MViewModelFactory(LibraryRepository(client))).get(
            CreateNewAdminViewModel::class.java
        )

        binding.progressBar.visibility = View.INVISIBLE

        binding.buttonSave.setOnClickListener {
            askAppointment()
        }
    }

    private fun askAppointment() {
        println("gende = $gender")
        val userId = binding.edId.text.toString()

        when {
            userId.isEmpty() -> {
                binding.edId.error = "NISN Tidak Boleh Kosong"
                binding.edId.requestFocus()
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
        // handling loading visibility
        viewModel.isLoading.observe(this) { boolean ->
            binding.progressBar.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
        }

        //pass data to view model
        viewModel.createNewAdmin(
            binding.edId.text.toString(),
            binding.edPassword.text.toString(),
            binding.edName.text.toString(),
            "admin",
            binding.edEmail.text.toString(),
            binding.edPhoneNumber.text.toString(),
            binding.edAddress.text.toString(),
            gender!!
        )

        // get fail message from view model
        viewModel.failMessage.observe(this) {
            if (it != null){
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.failMessage.value = null
                if (it == ""){
                    AlertDialog.Builder(this@CreateNewAdminActivity)
                        .setTitle("Success")
                        .setMessage("Berhasil Ditambahkan")
                        .setPositiveButton("Tutup") { _, _ ->
                            // clear all edit text
                        }
                        .show()
                    println("ada succesmessage")
                }
                else {
                    AlertDialog.Builder(this@CreateNewAdminActivity)
                        .setTitle("Gagal")
                        .setMessage(it)
                        .setPositiveButton("Tutup") { _, _ ->
                            //do nothing
                        }
                        .show()
                }
            }
        }
        
        // get error message from view model
        viewModel.errorMessage.observe(this) {
            binding.progressBar.visibility = View.GONE
            AlertDialog.Builder(this@CreateNewAdminActivity)
                .setTitle("ERROR")
                .setMessage(it)
                //close alert dialog
                .setPositiveButton("Tutup"){_,_ ->
                    //do nothing
                }
                .show()
        }
    }
}