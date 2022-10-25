package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityHomeBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook.UpdateBookFragment
import com.skripsi.perpustakaanapp.ui.login.LoginActivity
import com.skripsi.perpustakaanapp.ui.member.qrcode.QRCodeFragment
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity
import com.skripsi.perpustakaanapp.utils.NetworkInfo
import com.skripsi.perpustakaanapp.utils.WindowTouchableHelper
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel

    private var doubleBackPressed = false
    private var tempRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(RetrofitClient))).get(
            HomeViewModel::class.java
        )

        firstInitialization()
        setBottomNav()
    }

    private fun firstInitialization() {
        supportActionBar?.hide()
        setSupportActionBar(binding.myToolbar)

        sessionManager = SessionManager(this)

        if (intent?.extras!=null){
            binding.toolbarTitle.text = "Hi, ${intent?.getStringExtra("FIRST_NAME")}"
            userProfile()
            getDateNow()
        }
    }

    private fun userProfile() {
        Glide.with(this)
            .load(NetworkInfo.AVATAR_IMAGE_BASE_URL+intent.getStringExtra("AVATAR"))
            .signature(ObjectKey(System.currentTimeMillis().toString()))
            .centerCrop()
            .into(binding.toolbarIcon)
        binding.toolbarIcon.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.USERNAME, sessionManager.fetchUsername())
            startActivity(intent)
        }
    }

    private fun getDateNow() {
        val date = Calendar.getInstance().time
        val dateFormat = DateFormat.format("d MMMM yyyy", date) as String
        val today = DateFormat.format("EEEE", date) as String
        val dateNow = "$today, $dateFormat"

        binding.tvDate.text = dateNow
    }

    private fun setBottomNav() {
        showUserFragment()
        binding.bottomNav.menu.findItem(R.id.home).isChecked = true
        binding.bottomNav.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.bottom_nav_menu1 -> {
                    bottomMenu1Listener()
                    true
                }
                R.id.home -> {
                    showUserFragment()
                    true
                }
                R.id.bottom_nav_menu2 -> {
                    bottomMenu2Listener()
                    true
                }
                else -> false
            }
        }
    }

    private fun bottomMenu1Listener() {
        if (sessionManager.fetchUserRole() == "student") {
            loadFragment(QRCodeFragment())
        } else if (sessionManager.fetchUserRole() == "admin") {
            loadFragment(CardMenuFragment())
        }
    }

    private fun bottomMenu2Listener() {
        if (sessionManager.fetchUserRole() == "student") {
            loadFragment(UpdateBookFragment())
        } else if (sessionManager.fetchUserRole() == "admin") {
            loadFragment(UpdateBookFragment())
        }
    }

    private fun showUserFragment() {
        if (null == sessionManager.fetchAuthToken()) {
            return MySnackBar.showRed(binding.root, "Not Allowed Here")
        }
        if (sessionManager.fetchUserRole() == "admin") {
            return loadFragment(HomeAdminFragment())
        }
        if (sessionManager.fetchUserRole() == "student") {
            return loadFragment(HomeMemberFragment())
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed()
            return
        }
        doubleBackPressed = true
        MySnackBar.showWhite(binding.root, "Tekan Sekali Lagi Untuk Keluar")
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackPressed = false }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu -> {
                MyAlertDialog.showWith2Event(this, null, resources.getString(R.string.log_out_confirmation), resources.getString(R.string.confirmation_yes), resources.getString(R.string.confirmation_no),
                    {_,_ ->
                        userLogout()
                    },
                    {_,_ ->

                    })
                true
            }
            R.id.swap_menu -> {
                swapRole(item)
                true
            }
            else -> true
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (sessionManager.fetchUserRole() == "student") {
            val swapMenu = menu?.findItem(R.id.swap_menu)
            swapMenu?.isVisible = false
        }
        if (tempRole == "admin") {
            val swapMenu = menu?.findItem(R.id.swap_menu)
            swapMenu?.title = "Sebagai Admin"
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun userLogout() {
        viewModel.userLogout(token = sessionManager.fetchAuthToken().toString())

        viewModel.resourceLogout.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        WindowTouchableHelper.disable(this)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        WindowTouchableHelper.enable(this)
                        binding.progressBar.visibility = View.VISIBLE
                        startIntentBackToLogin()
                    }
                    is MyResource.Error -> {
                        WindowTouchableHelper.enable(this)
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.show(this, R.drawable.icon_cancel, "ERROR", resource.message.toString())
                    }
                }
            }
        }
    }

    private fun swapRole(item: MenuItem?) {
        if (!binding.bottomNav.menu.findItem(R.id.home).isChecked) {
            return MySnackBar.showRed(binding.root, "Pastikan Anda Berada di Dashboard / Home")
        }
        if (sessionManager.fetchUserRole() == "admin") {
            tempRole = sessionManager.fetchUserRole()
            sessionManager.saveUserRole("student")
            showUserFragment()
            item?.title = "Sebagai Admin"
        } else if (tempRole == "admin") {
            sessionManager.saveUserRole(tempRole.toString())
            tempRole = null
            showUserFragment()
            item?.title = "Sebagai Anggota"
        }
    }

    private fun startIntentBackToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}