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
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.databinding.ActivityLoginBinding
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.home.HomeAdminActivity
import com.skripsi.perpustakaanapp.ui.home.HomeUserActivity
import com.skripsi.perpustakaanapp.ui.register.RegisterActivity


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

        viewModel = ViewModelProvider(this, MViewModelFactory(LibraryRepository(client))).get(
            LoginViewModel::class.java
        )

        binding.progressBar.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.INVISIBLE
        binding.txtSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
        }
        //listener for login button
        binding.btnLogin.setOnClickListener {
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
            MyAlertDialog.showAlertDialog(this@LoginActivity, R.drawable.icon_no_connection, "No Internet Connection", "Periksa Data Seluler Atau WIFI Anda")
        }
    }

    //function for pass data login request to api
    private fun postLogin(username: String, password: String) {

        sessionManager = SessionManager(this)

        viewModel.isLoading.observe(this) { boolean ->
            binding.progressBar.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
        }

        viewModel.userLogin(username, password)

        //failed handling, if there is problem with database rules
        viewModel.failMessage.observe(this) { message ->
            //jika fail message ada isinya
            if (message != null) {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.failMessage.value = null
                //if fail message is "" but not null
                if (message == "") {
                    // for get role name
                    viewModel.roleName.observe(this) { roleName ->
                        if (roleName != null) {
                            sessionManager.saveUserRole(roleName)
                            if (roleName == "admin") {
                                viewModel.userName.observe(this){ username ->
                                    startIntentExtraData(this@LoginActivity, HomeAdminActivity::class.java, username)
                                }
                            }
                            else if (roleName == "student") {
                                viewModel.userName.observe(this){ username ->
                                    startIntentExtraData(this@LoginActivity, HomeUserActivity::class.java, username)
                                }
                            }
                        }
                    }
                } else {
                    MyAlertDialog.showAlertDialog(this@LoginActivity,R.drawable.icon_cancel,"Login Gagal",message)
                }
            }
        }

        //error handling, if there is problem with connection or server
        viewModel.errorMessage.observe(this) {
            if (it != null) {
                MyAlertDialog.showAlertDialog(this@LoginActivity, R.drawable.icon_cancel, "ERROR", it)
                viewModel.errorMessage.value = null
            }
        }

        viewModel.token.observe(this) {
            if (it != null) {
                sessionManager.saveAuthToken(it)
            }
        }
    }

    private fun startIntentExtraData(activity: Activity, cls: Class<*>, username: String?) {
        val home = Intent(activity, cls)
        home.putExtra("user_name", username)
        startActivity(home)
        finish()
    }
}