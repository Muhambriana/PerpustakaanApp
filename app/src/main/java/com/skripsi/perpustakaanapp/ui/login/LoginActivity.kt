package com.skripsi.perpustakaanapp.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.databinding.ActivityLoginBinding
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.home.HomeAdminActivity
import com.skripsi.perpustakaanapp.ui.home.HomeUserActivity
import com.skripsi.perpustakaanapp.ui.member.register.RegisterActivity
import com.skripsi.perpustakaanapp.utils.WindowTouchableHelper
import com.skripsi.perpustakaanapp.utils.setSingleClickListener


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: LoginViewModel

    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            LoginViewModel::class.java
        )

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
            MyAlertDialog.showAlertDialog(this, R.drawable.icon_no_connection, "No Internet Connection", "Periksa Data Seluler Atau WIFI Anda")
        }
    }

    //function for pass data login request to api
    private fun postLogin(username: String, password: String) {

        sessionManager = SessionManager(this)

        viewModel.userLogin(username, password)

        viewModel.resourceLogin.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        WindowTouchableHelper.disable(this)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        WindowTouchableHelper.enable(this)
                        binding.progressBar.visibility = View.GONE
                        startIntentDashboard(
                            resource.data?.roleName.toString(),
                            resource.data?.username.toString(),
                            "Bearer ${resource.data?.token.toString()}",
                            resource.data?.firstName.toString())
                    }
                    is Resource.Error ->  {
                        WindowTouchableHelper.enable(this)
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(this,R.drawable.icon_cancel,"Login Gagal", resource.message.toString())
                    }
                }
            }
        }
    }

    private fun startIntentDashboard(roleName: String, username:String, token: String, firstName: String,) {
        // Save to SharedPreferences
        sessionManager.saveUserRole(roleName)
        sessionManager.saveUsername(username)
        sessionManager.saveAuthToken(token)

        // Start Activity
        if (roleName == "admin") {
            startIntentExtraData(this, HomeAdminActivity::class.java, firstName)
        }
        else if (roleName == "student") {
            startIntentExtraData(this, HomeUserActivity::class.java, firstName)
        }
    }

    private fun startIntentExtraData(activity: Activity, cls: Class<*>, firstname: String?) {
        val home = Intent(activity, cls)
        home.putExtra("first_name", firstname)
        startActivity(home)
        finish()
    }
}