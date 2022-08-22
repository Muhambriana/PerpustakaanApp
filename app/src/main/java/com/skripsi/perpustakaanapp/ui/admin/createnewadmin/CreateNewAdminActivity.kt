package com.skripsi.perpustakaanapp.ui.admin.createnewadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityCreateNewAdminBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.setSingleClickListener

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
        // handling loading visibility
        viewModel.isLoading.observe(this) { boolean ->
            binding.progressBar.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
        }

        //pass data to view model
        viewModel.createNewAdmin(
            binding.edUsername.text.toString(),
            binding.edtPassword.text.toString(),
            binding.edtName.text.toString(),
            "admin",
            binding.edEmail.text.toString(),
            binding.edtTelNumber.text.toString(),
            binding.edtAddress.text.toString(),
            gender!!
        )

        // get fail message from view model
        viewModel.responseMessage.observe(this) { message ->
            if (message != null){
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.responseMessage.value = null
                if (message == "success"){
                    MyAlertDialog.showAlertDialogEvent(this@CreateNewAdminActivity, R.drawable.icon_checked, "SUCCESS", "Berhasil Membuat Admin Baru")
                    {_, _ ->
//                        binding.edUsername.text?.clear()
                    }
                }
                else {
                    MyAlertDialog.showAlertDialog(this@CreateNewAdminActivity, R.drawable.icon_cancel, "FAILED", message)
                }
            }
        }
        
        // get error message from view model
        viewModel.errorMessage.observe(this) {
            if (it != null) {
                viewModel.errorMessage.value  = null
                MyAlertDialog.showAlertDialog(this@CreateNewAdminActivity, R.drawable.icon_cancel, "ERROR", it)
            }
        }
    }
}