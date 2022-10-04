package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.scanattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var binding: ActivityScannerBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ScannerViewModel

    private val client = RetrofitClient
    private var zXingScannerView: ZXingScannerView? = null
    private var purpose: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        purpose = getPurpose()
        openScanner()
    }

    private fun firstInitialization() {
        binding.progressBar.visibility = View.INVISIBLE
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            ScannerViewModel::class.java
        )
    }

    private fun openScanner() {
        zXingScannerView = ZXingScannerView(this)
        binding.cameraView.addView(zXingScannerView)
        readQR()
    }

    private fun readQR() {
        zXingScannerView?.startCamera()
        zXingScannerView?.setResultHandler(this)
        zXingScannerView?.setAutoFocus(true)
    }

    private fun getPurpose(): Int? {
        binding.rbAttendance.setOnCheckedChangeListener { _, i ->
            purpose =
                when(i) {
                    binding.rbAttendanceIn.id -> 0
                    binding.rbAttendanceOut.id -> 1
                    else -> -1
                }
        }
        return purpose
    }

    override fun handleResult (result: Result) {

        postAttendance(result.text)
        binding.textview.text = result.text
    }

    private fun postAttendance(qrCode: String) {
        zXingScannerView?.stopCameraPreview()
        if (purpose == null ) {
            MyAlertDialog.showAlertDialogEvent(this, R.drawable.icon_checked, "Pilih Tujuan Terlebih Dahulu", ""){ _, _ ->
                zXingScannerView?.resumeCameraPreview(this)
            }

        } else {
            viewModel.scannerAttendance(sessionManager.fetchAuthToken().toString(), qrCode, purpose)
        }

        viewModel.resourceScanner.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialogEvent(this,
                            R.drawable.icon_checked,
                            "Success",
                            resource.data.toString()) { _, _ ->
                            askUser()
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialogEvent(this, R.drawable.icon_cancel, "Failed", resource.message.toString()){ _, _ -> }
                    }
                }
            }
        }

    }

    private fun askUser() {
        MyAlertDialog.showAlertDialog2Event(this, R.drawable.icon_cancel, "??", "Lanjutkan Scan?", { _, _ ->
          zXingScannerView?.resumeCameraPreview(this)
        }, {_,_ ->
            finish()
        })

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