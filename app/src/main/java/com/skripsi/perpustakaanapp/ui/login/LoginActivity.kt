package com.skripsi.perpustakaanapp.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.LoginResponse
import com.skripsi.perpustakaanapp.databinding.ActivityLoginBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.home.HomeActivity
import com.skripsi.perpustakaanapp.ui.member.register.RegisterActivity
import com.skripsi.perpustakaanapp.utils.WindowTouchableHelper
import com.skripsi.perpustakaanapp.utils.setSingleClickListener


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: LoginViewModel

    private val client = RetrofitClient
    private var dataLogin: LoginResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firstInitialization()
        clickListener()
    }

    private fun firstInitialization() {
        // Disable dark mode in all over the app
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            LoginViewModel::class.java
        )

        hideShowPassword()
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
        binding.txtSignUp.setSingleClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        //listener for login button
        binding.btnLogin.setSingleClickListener {
            askAppointment()
        }
    }

    private fun askAppointment() {
        val username = binding.edtUsername.text.toString()
        val password = binding.edtPassword.text.toString()
        when {
            username.isEmpty() -> {
                binding.edtUsername.error = "Nomor Induk Siswa Tidak Boleh Kosong"
                binding.edtUsername.requestFocus()
            }
            password.isEmpty() -> {
                binding.edtPassword.error = "Password Tidak Boleh Kosong"
                binding.edtPassword.requestFocus()
            }
            else -> {
                isConnect(username, password)
            }
        }
    }

    //check connectivity available
    private fun isConnect(username: String, password: String){
        val connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            //post login data to API
            postLogin(username, password)
        } else {
            MyAlertDialog.show(this, R.drawable.icon_no_connection, "No Internet Connection", "Periksa Data Seluler Atau WIFI Anda")
        }
    }

    //function for pass data login request to api
    private fun postLogin(username: String, password: String) {

        sessionManager = SessionManager(this)

        viewModel.userLogin(username, password)

        viewModel.resourceLogin.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        WindowTouchableHelper.disable(this)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        WindowTouchableHelper.enable(this)
                        binding.progressBar.visibility = View.GONE
                        dataLogin = resource.data
                        startIntentDashboard()
                    }
                    is MyResource.Error ->  {
                        WindowTouchableHelper.enable(this)
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.show(this,R.drawable.icon_cancel,"Login Gagal", resource.message.toString())
                    }
                }
            }
        }
    }

    private fun startIntentDashboard() {

        val roleName: String? = dataLogin?.roleName
        val firstName: String? = dataLogin?.firstName
        val token = "Bearer ${dataLogin?.token}"

        // Save to SharedPreferences
        if (roleName != null) {
            sessionManager.saveUserRole(roleName)
        }
        sessionManager.saveUsername(dataLogin?.username.toString())
        sessionManager.saveAuthToken(token)
        sessionManager.saveQRCode(dataLogin?.qrCode.toString())

        // Start Activity
        if (roleName == "admin") {
            startIntentExtraData(this, HomeActivity::class.java, firstName)
        }
        else if (roleName == "student") {
            startIntentExtraData(this, HomeActivity::class.java, firstName)
        }
    }

    private fun startIntentExtraData(activity: Activity, cls: Class<*>, firstname: String?) {
        val home = Intent(activity, cls)
        home.putExtra("FIRST_NAME", firstname)
        home.putExtra("AVATAR", dataLogin?.avatar)
        startActivity(home)
        finish()
    }
}