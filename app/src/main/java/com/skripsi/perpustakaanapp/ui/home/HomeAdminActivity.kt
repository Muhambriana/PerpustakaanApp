package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityHomeAdminBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook.CreateBookActivity
import com.skripsi.perpustakaanapp.ui.admin.listuser.UserActivity
import com.skripsi.perpustakaanapp.ui.admin.pendingloan.PendingLoanActivity
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.createnewadmin.CreateNewAdminActivity
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.scanattendance.ScannerActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity
import com.skripsi.perpustakaanapp.ui.login.LoginActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity
import com.skripsi.perpustakaanapp.utils.NetworkInfo


class HomeAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel
    private var doubleBackPressed = false

    private val client = RetrofitClient

    var drawerLayout: DrawerLayout? = null
    var navigationView: NavigationView? = null
    var slideState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSideNavigation()
        firstInitialization()
        cardListener()
    }

    private fun setSideNavigation() {
        drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawerLayout?.setDrawerListener(object : ActionBarDrawerToggle(this,
            drawerLayout,
            null,
            0,
            0) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                slideState = false //is Closed
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                slideState = true //is Opened
            }
        })

        navigationView = findViewById<View>(R.id.nav_view) as NavigationView

        // if user select item from the navigation view it will be detected here

        // if user select item from the navigation view it will be detected here
        navigationView?.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item1 -> {
                    val intent = Intent(this, BookActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item2 -> {
                    println("item 2 selected")
                    true
                } else -> false
            }

        }
    }

    private fun firstInitialization() {
        supportActionBar?.hide()
        setSupportActionBar(binding.myToolbar)

        sessionManager = SessionManager(this)

        if (intent.extras!=null){
            binding.toolbarTitle.text = "Hi, ${intent.getStringExtra("FIRST_NAME")}"
            userProfile()
        }

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            HomeViewModel::class.java
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu -> {
                userLogout()
                true
            }
            R.id.side_bar_nav -> {
                if(slideState){
                    drawerLayout?.closeDrawer(Gravity.RIGHT)
                }else {
                    drawerLayout?.openDrawer(Gravity.RIGHT)
                }
                true
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed()
            return
        }
        doubleBackPressed = true
        Snackbar.make(binding.root, "Tekan Sekali Lagi Untuk Keluar", Snackbar.LENGTH_LONG)
            .show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackPressed = false }, 2000)
    }

    private fun cardListener() {
        val clickListener = View.OnClickListener { view ->
            when (view.id){
                binding.cardCreateBook.id -> {
                    val intent = Intent(this, CreateBookActivity::class.java)
                    startActivity(intent)
                }
                binding.cardCreateAdmin.id -> {
                    val intent = Intent(this, CreateNewAdminActivity::class.java)
                    startActivity(intent)
                }
                binding.cardBookList.id -> {
                    val intent = Intent(this, BookActivity::class.java)
                    startActivity(intent)
                }
                binding.cardPendingLoanList.id -> {
                    val intent = Intent(this, PendingLoanActivity::class.java)
                    startActivity(intent)
                }
                binding.cardUserList.id -> {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                }
                binding.cardScanVisitors.id -> {
                    val intent = Intent(this, ScannerActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.cardCreateBook.setOnClickListener(clickListener)
        binding.cardCreateAdmin.setOnClickListener(clickListener)
        binding.cardBookList.setOnClickListener(clickListener)
        binding.cardPendingLoanList.setOnClickListener(clickListener)
        binding.cardUserList.setOnClickListener(clickListener)
        binding.cardScanVisitors.setOnClickListener(clickListener)
    }

    private fun userLogout() {

        viewModel.userLogout(sessionManager.fetchAuthToken().toString())

        viewModel.resourceLogout.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        startIntentBackToLogin()
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.show(this, R.drawable.icon_cancel, "ERROR", resource.message.toString())
                    }
                }
            }
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

    private fun startIntentBackToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


}