package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.scanattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.databinding.ActivityScannerBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackbar
import com.skripsi.perpustakaanapp.utils.PermissionCheck
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var binding: ActivityScannerBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ScannerViewModel

    private val client = RetrofitClient
    private var zXingScannerView: ZXingScannerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        if (PermissionCheck.camera(this)) {
            zXingScannerView = ZXingScannerView(this)
            setContentView(zXingScannerView)
            readQR()
        }

        firstInitialization()
//        openScanner()
    }

    private fun firstInitialization() {
        binding.progressBar.visibility = View.INVISIBLE
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            ScannerViewModel::class.java
        )
    }

    private fun openScanner() {
        if (PermissionCheck.camera(this)) {
            zXingScannerView = ZXingScannerView(this)
            setContentView(zXingScannerView)
            readQR()
        } else {
            openScanner()
        }
    }

    private fun readQR() {
        zXingScannerView?.startCamera()
        zXingScannerView?.setResultHandler(this)
        zXingScannerView?.setAutoFocus(true)
    }

    override fun handleResult (result: Result) {
        postAttendance(result.text)
        Handler(Looper.getMainLooper()).postDelayed(Runnable { zXingScannerView?.resumeCameraPreview(this) }, 3000)
    }

    private fun postAttendance(qrCode: String) {
        zXingScannerView?.stopCameraPreview()

        viewModel.scannerAttendance(sessionManager.fetchAuthToken().toString(), qrCode)

        viewModel.resourceScanner.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackbar.showSnackBar(binding.root, resource.data.toString())
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackbar.showSnackBar(binding.root, resource.message.toString())
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

    companion object {
        private const val REQUEST_CODE_IMAGE = 201
        private const val REQUEST_CODE_PERMISSION = 202
    }
}