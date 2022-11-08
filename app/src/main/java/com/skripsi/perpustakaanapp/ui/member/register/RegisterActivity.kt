package com.skripsi.perpustakaanapp.ui.member.register

import android.os.Bundle
import android.text.InputFilter
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityRegisterBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    private val client = RetrofitClient
    private var gender: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        clickListener()
    }

    private fun firstInitialization() {
        supportActionBar?.title = "Register"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            RegisterViewModel::class.java
        )

        binding.progressBar.visibility = View.INVISIBLE

        gender = getGender()

        editTextFilter()
    }

    private fun editTextFilter() {
        hideShowPassword()
        binding.edtFirstName.filters += InputFilter.AllCaps()
        binding.edtLastName.filters += InputFilter.AllCaps()
    }

    private fun hideShowPassword() {
        binding.buttonHideShow.setOnClickListener {
            if(binding.edtPassword.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
                binding.buttonHideShow.setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance();
                binding.edtPassword.setSelection(binding.edtPassword.length())
            }
            else{
                binding.buttonHideShow.setImageResource(R.drawable.ic_visibility_on);
                //Hide Password
                binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance();
                binding.edtPassword.setSelection(binding.edtPassword.length())
            }
        }
    }

    private fun clickListener() {
        binding.buttonSave.setSingleClickListener {
            askAppointment()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun askAppointment() {
        val username = binding.edtUsername.text.toString()

        when {
            username.isEmpty() -> {
                binding.edtUsername.error = "NISN Tidak Boleh Kosong"
                binding.edtUsername.requestFocus()
            }
            binding.rbGender.checkedRadioButtonId == -1 -> {
                MyAlertDialog.show(this,
                    R.drawable.icon_warning,
                    "WARNING",
                    "Pilih Jenis Kelamin Terlebih Dahulu")
            }
            else -> {
                postUserData()
            }
        }
    }

    private fun postUserData() {

        viewModel.registerUser(
            binding.edtUsername.text.toString(),
            binding.edtPassword.text.toString(),
            binding.edtFirstName.text.toString(),
            binding.edtLastName.text.toString(),
            "student",
            binding.edEmail.text.toString(),
            binding.edtTelNumber.text.toString(),
            binding.edtAddress.text.toString(),
            gender!!
        )

        viewModel.resourceRegisterUser.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showWithEvent(this@RegisterActivity,
                            R.drawable.icon_checked,
                            it.data.toString().uppercase(),
                            "Berhasil Mendaftar") { _, _ ->
                        }
                        clearEditText()
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.show(this@RegisterActivity,
                            R.drawable.icon_cancel,
                            "FAILED",
                            it.message.toString())
                    }
                }
            }
        }
    }

    private fun clearEditText() {
        binding.edtUsername.text.clear()
        binding.edtPassword.text.clear()
        binding.edtFirstName.text.clear()
        binding.edtLastName.text.clear()
        binding.edEmail.text.clear()
        binding.edtTelNumber.text.clear()
        binding.edtAddress.text.clear()
        gender = null
        binding.rbGender.clearCheck()
    }

    private fun getGender(): Int? {
        binding.rbGender.setOnCheckedChangeListener { _, i ->
            gender =
                when (i) {
                    binding.rbGenderMale.id -> 1
                    binding.rbGenderFemale.id -> 2
                    else -> -1
                }
        }
        return gender
    }
}