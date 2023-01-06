package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.createnewadmin

import android.os.Bundle
import android.text.InputFilter
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityCreateNewAdminBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class CreateNewAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNewAdminBinding
    private lateinit var viewModel: CreateNewAdminViewModel
    private lateinit var sessionManager: SessionManager

    private val client = RetrofitClient
    private var gender: Int? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        clickListener()
    }

    private fun firstInitialization() {
        supportActionBar?.title = "Tambah Admin"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        editTextFilter()
        prepDropdownList()
    }

    private fun editTextFilter() {
        hideShowPassword()
        binding.edtFirstName.filters += InputFilter.AllCaps()
        binding.edtLastName.filters += InputFilter.AllCaps()
    }

    private fun prepDropdownList() {
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.edu_level_admin,
            R.layout.custom_spinner_text)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)

        binding.spinnerEduLevel.adapter = adapter
    }


    private fun hideShowPassword() {
        binding.buttonHideShow.setOnClickListener {
            if(binding.edtPassword.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
                binding.buttonHideShow.setImageResource(R.drawable.ic_visibility_off)
                //Show Password
                binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.edtPassword.setSelection(binding.edtPassword.length())
            }
            else{
                binding.buttonHideShow.setImageResource(R.drawable.ic_visibility_on)
                //Hide Password
                binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.edtPassword.setSelection(binding.edtPassword.length())
            }
        }
    }

    private fun clickListener() {
        binding.buttonSave.setSingleClickListener {
            askAppointment()
        }
    }

    private fun askAppointment() {
        val username = binding.edtUsername.text.toString()
        val password = binding.edtPassword.text.toString()
        when {
            username.isEmpty() -> {
                binding.edtUsername.error = "NISN Tidak Boleh Kosong"
                binding.edtUsername.requestFocus()
            }
            password.isEmpty() -> {
                binding.edtPassword.error = "Password Tidak Boleh Kosong"
            }
            binding.rbGender.checkedRadioButtonId == -1 -> {
                MySnackBar.showRed(binding.root, "Pilih Gender Terlbih Dahulu")
            }
            binding.spinnerEduLevel.selectedItemPosition == 0 -> {
                binding.spinnerEduLevel.requestFocus()
                MySnackBar.showRed(binding.root, "Pilih Jenjang Terlebih Dahulu")
            }
            else -> {
                MyAlertDialog.showWith2Event(
                    this,
                    null,
                    resources.getString(R.string.data_confirmation),
                    resources.getString(R.string.confirmation_yes),
                    resources.getString(R.string.confirmation_recheck),
                    {_,_ ->
                        postUserData()
                    }, {_,_ ->

                    })
            }
        }
    }

    private fun postUserData() {
        sessionManager = SessionManager(this)
        //pass data to view model
        gender?.let {
            viewModel.createNewAdmin(
                sessionManager.fetchAuthToken().toString(),
                binding.edtUsername.text.toString(),
                binding.edtPassword.text.toString(),
                binding.edtFirstName.text.toString(),
                binding.edtLastName.text.toString(),
                "admin",
                binding.edtEmail.text.toString(),
                binding.edtPhoneNo.text.toString(),
                binding.edtAddress.text.toString(),
                it,
                binding.spinnerEduLevel.selectedItem.toString()
            )
        }

        viewModel.resourceCreateAdmin.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        clearEditText()
                        MySnackBar.showBlack(binding.root, resource.data.toString())
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showRed(binding.root, resource.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun clearEditText() {
        binding.edtUsername.text.clear()
        binding.edtPassword.text.clear()
        binding.edtFirstName.text.clear()
        binding.edtLastName.text.clear()
        binding.edtEmail.text.clear()
        binding.edtPhoneNo.text.clear()
        binding.edtAddress.text.clear()
        gender = null
        binding.rbGender.clearCheck()
        binding.spinnerEduLevel.setSelection(0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }
}