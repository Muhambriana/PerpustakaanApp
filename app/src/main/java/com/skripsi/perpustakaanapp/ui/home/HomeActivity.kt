package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
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
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.bookcategory.BookCategoryFragment
import com.skripsi.perpustakaanapp.ui.admin.scanner.ScannerAttendanceFragment
import com.skripsi.perpustakaanapp.ui.login.LoginActivity
import com.skripsi.perpustakaanapp.ui.member.qrcode.QRCodeFragment
import com.skripsi.perpustakaanapp.ui.statistik.StatisticFragment
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity
import com.skripsi.perpustakaanapp.utils.NetworkInfo.AVATAR_IMAGE_BASE_URL
import com.skripsi.perpustakaanapp.utils.WindowTouchableHelper
import java.util.*


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var doubleBackPressed = false
    private var tempRole: String? = null
    private var avatar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(RetrofitClient))).get(
            HomeViewModel::class.java
        )

        firstInitialization()
        setLauncher()
        setBottomNav()
    }

    private fun firstInitialization() {
        supportActionBar?.hide()
        setSupportActionBar(binding.myToolbar)
        avatar = intent.getStringExtra("AVATAR")

        sessionManager = SessionManager(this)

        if (intent?.extras!=null){
            binding.toolbarTitle.text = "Hi, ${intent?.getStringExtra("FIRST_NAME")}"
            userAvatar()
            getDateNow()
        }
    }

    private fun setLauncher() {
        //For get return data after launch activity
        resultLauncher =  registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if (result.resultCode == RESULT_OK) {
                avatar = sessionManager.fetchUsername()+".png"
                //Re-run getBookData and update with the latest
                userAvatar()
            }
        }
    }

    private fun userAvatar() {
        binding.toolbarIcon.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.USERNAME, sessionManager.fetchUsername())
            resultLauncher.launch(intent)
        }
        if (avatar == null) {
            binding.toolbarIcon.background = ContextCompat.getDrawable(this, R.drawable.icon_user)
            return
        }
        glideSetup(avatar)
    }

    private fun glideSetup(imageName: String?) {
        val imageUrl = GlideUrl(AVATAR_IMAGE_BASE_URL+imageName) { mapOf(Pair("Authorization", sessionManager.fetchAuthToken())) }

        Glide.with(this)
            .load(imageUrl)
            .signature(ObjectKey(System.currentTimeMillis().toString()))
            .override(100, 400)
            .centerCrop()
            .into(binding.toolbarIcon)
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
                    loadFragment(StatisticFragment())
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
            loadFragment(ScannerAttendanceFragment())
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
            R.id.add_category -> {
                BookCategoryFragment().show(supportFragmentManager, "UpdateBookFragment")
                true
            }
            else -> true
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val swapMenu = menu?.findItem(R.id.swap_menu)
        if (sessionManager.fetchUserRole() == "student") {
            swapMenu?.isVisible = false
            menu?.findItem(R.id.add_category)?.isVisible = false

        }
        if (tempRole == "admin") {
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