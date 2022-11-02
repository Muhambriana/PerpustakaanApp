package com.skripsi.perpustakaanapp.ui.admin.scanner

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentScannerAttendanceBinding
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.utils.PermissionCheck
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerAttendanceFragment : Fragment(), ZXingScannerView.ResultHandler {

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ScannerViewModel

    private val client = RetrofitClient
    private var zXingScannerView: ZXingScannerView? = null
    private var fragmentScannerAttendanceBinding: FragmentScannerAttendanceBinding? = null
    private val binding get() = fragmentScannerAttendanceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentScannerAttendanceBinding = FragmentScannerAttendanceBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (PermissionCheck.camera(requireActivity())) {
            zXingScannerView = ZXingScannerView(requireContext())
            binding?.cameraView?.addView(zXingScannerView)
            readQR()
        }

        firstInitialization()
    }

    private fun firstInitialization() {
        binding?.progressBar?.visibility = View.INVISIBLE

        sessionManager = SessionManager(requireContext())

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            ScannerViewModel::class.java
        )
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

        viewModel.scannerAttendance(sessionManager.fetchAuthToken().toString(), qrCode, sessionManager.fetchUsername().toString())

        viewModel.resourceScanner.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        MySnackBar.showWhite(binding?.root, resource.data.toString().uppercase())
                    }
                    is MyResource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        MySnackBar.showRed(binding?.root, resource.message.toString())
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