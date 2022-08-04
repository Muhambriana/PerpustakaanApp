package com.skripsi.perpustakaanapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.databinding.ActivityLoginBinding
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
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
//            RegisterActivity().show(supportFragmentManager, "aa")
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
                postLogin(username, password)
            }
        }
    }

    //function for pass data login request to api
    private fun postLogin(nis: String, password: String) {

        sessionManager = SessionManager(this)

        viewModel.isLoading.observe(this) { boolean ->
            binding.progressBar.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
        }

        viewModel.userLogin(nis, password)

        viewModel.failMessage.observe(this) { message ->
            //jika fail message ada isinya
            if (message != null) {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.failMessage.value = null

                //jika isi fail message adalah kosong
                if (message == "") {
                    // for get role name
                    viewModel.roleName.observe(this) { roleName ->
                        if (roleName != null) {
                            sessionManager.saveUserRole(roleName)
                            if (roleName == "admin") {
                                viewModel.userName.observe(this){ username ->
                                    val home = Intent(this@LoginActivity, HomeAdminActivity::class.java)
                                    home.putExtra("user_name", username)
                                    startActivity(home)
                                    finish()
                                }
                            }
                            else if (roleName == "student") {
                                viewModel.userName.observe(this){ username ->
                                    val home = Intent(this@LoginActivity, HomeUserActivity::class.java)
                                    home.putExtra("user_name", username)
                                    startActivity(home)
                                    finish()
                                }
                            }
                        }
                    }
                } else {
                        AlertDialog.Builder(this@LoginActivity)
                            .setTitle("Login Gagal")
                            .setMessage(message)
                            .setPositiveButton("Tutup") { _, _ ->
                                //do nothing
                            }
                            .show()
                }
            }
        }

        viewModel.token.observe(this) {
            if (it != null) {
                sessionManager.saveAuthToken(it)
            }
        }

        viewModel.errorMessage.observe(this) {
            binding.progressBar.visibility = View.GONE
            AlertDialog.Builder(this@LoginActivity)
                .setTitle("ERROR")
                .setMessage(it)
                .setPositiveButton("Tutup") { _, _ ->
                    // do nothing
                }
                .show()
        }
    }
}