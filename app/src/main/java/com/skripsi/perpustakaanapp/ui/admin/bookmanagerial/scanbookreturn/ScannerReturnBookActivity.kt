package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.scanbookreturn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityScannerBinding
import com.skripsi.perpustakaanapp.databinding.ActivityScannerReturnBookBinding
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.scanattendance.ScannerViewModel
import com.skripsi.perpustakaanapp.utils.PermissionCheck
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerReturnBookActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var binding: ActivityScannerReturnBookBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ScannerReturnBookViewModel

    private val client = RetrofitClient
    private var zXingScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerReturnBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (PermissionCheck.camera(this)) {
            zXingScannerView = ZXingScannerView(this)
            binding.cameraView.addView(zXingScannerView)
            readQR()
        }

        firstInitialization()
    }

    private fun firstInitialization() {
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.progressBar.visibility = View.INVISIBLE

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            ScannerReturnBookViewModel::class.java
        )
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        onBackPressed()
        super.supportNavigateUpTo(upIntent)
    }

    private fun readQR() {
        zXingScannerView?.startCamera()
        zXingScannerView?.setResultHandler(this)
        zXingScannerView?.setAutoFocus(true)
    }

    override fun handleResult (result: Result) {
        postAttendance(result.text)
        Handler(Looper.getMainLooper()).postDelayed({ zXingScannerView?.resumeCameraPreview(this) }, 2000)
    }

    private fun postAttendance(qrCode: String) {
        zXingScannerView?.stopCameraPreview()

        viewModel.scannerReturning(sessionManager.fetchAuthToken().toString(), qrCode)

        viewModel.resourceScanner.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showWhite(binding.root, resource.data.toString().uppercase())
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showRed(binding.root, resource.message.toString())
                    }
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        zXingScannerView?.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        zXingScannerView?.stopCamera()
    }
}