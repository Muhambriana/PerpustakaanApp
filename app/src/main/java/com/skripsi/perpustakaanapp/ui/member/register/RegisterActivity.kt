package com.skripsi.perpustakaanapp.ui.member.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.databinding.ActivityRegisterBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    private val client = RetrofitClient

    private var gender: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Register"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rbGender.setOnCheckedChangeListener { _, i ->
            gender =
                when (i) {
                    R.id.rb_gender_male -> 1
                    R.id.rb_gender_female -> 0
                    else -> -1
                }
        }


        viewModel = ViewModelProvider(this, MViewModelFactory(LibraryRepository(client))).get(
            RegisterViewModel::class.java
        )

        binding.progressBar.visibility = View.INVISIBLE

        binding.buttonSave.setOnClickListener {
            askAppointment()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun askAppointment() {
        val nisn = binding.edtNisn.text.toString()

        when {
            nisn.isEmpty() -> {
                binding.edtNisn.error = "NISN Tidak Boleh Kosong"
                binding.edtNisn.requestFocus()
            }
            binding.rbGender.checkedRadioButtonId == -1 -> {
                MyAlertDialog.showAlertDialog(this@RegisterActivity,
                    R.drawable.icon_warning,
                    "WARNING",
                    "Pilih Gender Terlebih Dahulu")
            }
            else -> {
                postUserData()
            }
        }
    }

    private fun postUserData() {

        viewModel.registerUser(
            binding.edtNisn.text.toString(),
            binding.edtPassword.text.toString(),
            binding.edtName.text.toString(),
            "student",
            binding.edEmail.text.toString(),
            binding.edtTelNumber.text.toString(),
            binding.edtAddress.text.toString(),
            gender!!
        )

        viewModel.resourceRegisterUser.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialogEvent(this@RegisterActivity,
                            R.drawable.icon_checked,
                            it.data.toString().uppercase(),
                            "Berhasil Mendaftar") { _, _ ->
//                            finish()
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(this@RegisterActivity,
                            R.drawable.icon_cancel,
                            "FAILED",
                            it.message.toString())
                    }
                }
            }
        }
    }
}


// bikin function 7untuk alert dialog
// yang dioper adalah icon dan warna, kalo ga da berrati yang dioper null

//class RegisterActivity : BottomSheetDialogFragment() {
//
//    private lateinit var binding: ActivityRegisterBinding
//    private lateinit var viewModel: RegisterViewModel
//
//    private val client = RetrofitClient
//
//    var gender: Int? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        binding = ActivityRegisterBinding.inflate(layoutInflater)
//        binding.rbGender.setOnCheckedChangeListener { _, i ->
//            gender =
//                when(i){
//                    R.id.rb_gender_male -> 1
//                    R.id.rb_gender_female -> 0
//                    else -> -1
//                }
//        }
//
//
//        viewModel = ViewModelProvider(this, MViewModelFactory(LibraryRepository(client))).get(
//            RegisterViewModel::class.java
//        )
//
//        binding.progressBar.visibility = View.INVISIBLE
//
//        binding.buttonSave.setOnClickListener {
//            askAppointment()
//        }
//
//        return binding.root
//    }
//
//    private fun askAppointment() {
//        println("gende = $gender")
//        val NISN = binding.edNisn.text.toString()
//
//        when {
//            NISN.isEmpty() -> {
//                binding.edNisn.error = "NISN Tidak Boleh Kosong"
//                binding.edNisn.requestFocus()
//            }
//            binding.rbGender.checkedRadioButtonId.equals(-1) -> {
//                context?.let {
//                    AlertDialog.Builder(it)
//                        .setTitle("Warning")
//                        .setMessage("Pilih Gender Terlebih Dahulu")
//                        .setPositiveButton("Tutup") { _, _ ->
//                            // do nothing
//                        }
//                        .show()
//                }
//            }
//            else -> {
//                postUserData()
//            }
//        }
//    }
//
//    private fun postUserData() {
//        println("satu")
//        viewModel.isLoading.observe(this) { boolean ->
//            binding.progressBar.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
//        }
//
//        viewModel.registerUser(
//            binding.edNisn.text.toString(),
//            binding.edPassword.text.toString(),
//            binding.edName.text.toString(),
//            "student",
//            binding.edEmail.text.toString(),
//            binding.edPhoneNumber.text.toString(),
//            binding.edAddress.text.toString(),
//            gender!!
//        )
//
//        viewModel.failMessage.observe(this) {
//            if (it != null){
//                //Reset status value at first to prevent multitriggering
//                //and to be available to trigger action again
//                viewModel.failMessage.value = null
//                if (it == ""){
//
//                    context?.let { it1 ->
//                        AlertDialog.Builder(it1)
//                            .setTitle("SUCCESS")
//                            .setMessage("Berhasil Mendaftar")
//                            .setPositiveButton("Login") { _, _ ->
//                                activity?.supportFragmentManager
//                                    ?.beginTransaction()
//                                    ?.remove(this)
//                                    ?.commit()
//                            }
//                            .show()
//                    }
//                    println("ada succesmessage")
//                }
//                else {
//                    context?.let { it1 ->
//                        AlertDialog.Builder(it1)
//                            .setTitle("Gagal")
//                            .setMessage(it)
//                            .setPositiveButton("Tutup") { _, _ ->
//                                //do nothing
//                            }
//                            .show()
//                    }
//                }
//            }
//        }
//
//        viewModel.errorMessage.observe(this) {
//            binding.progressBar.visibility = View.GONE
//            context?.let { it1 ->
//                AlertDialog.Builder(it1)
//                    .setTitle("ERROR")
//                    .setMessage(it)
//                    //close alert dialog
//                    .setPositiveButton("Tutup"){_,_ ->
//                        //do nothing
//                    }
//                    .show()
//            }
//        }
//    }
//}