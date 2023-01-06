package com.skripsi.perpustakaanapp.ui.admin.scanner

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentScannerReturningBookBinding
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.utils.PermissionCheck
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerReturningBookFragment : Fragment(), ZXingScannerView.ResultHandler {

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ScannerReturnBookViewModel

    private val client = RetrofitClient
    private var zXingScannerView: ZXingScannerView? = null
    private var fragmentScannerReturningBookBinding: FragmentScannerReturningBookBinding? = null
    private val binding get() = fragmentScannerReturningBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentScannerReturningBookBinding = FragmentScannerReturningBookBinding.inflate(layoutInflater, container, false)
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
            ScannerReturnBookViewModel::class.java
        )
    }

    private fun readQR() {
        zXingScannerView?.startCamera()
        zXingScannerView?.setResultHandler(this)
        zXingScannerView?.setAutoFocus(true)
    }

    override fun handleResult (result: Result) {
        postReturnBook(result.text)
        Handler(Looper.getMainLooper()).postDelayed({ zXingScannerView?.resumeCameraPreview(this) }, 2000)
    }

    private fun postReturnBook(qrCode: String) {
        zXingScannerView?.stopCameraPreview()

        viewModel.scannerReturning(sessionManager.fetchAuthToken().toString(), qrCode)

        viewModel.resourceScanner.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        if (resource.data?.code == 0) {
                            binding?.progressBar?.visibility = View.GONE
                            MySnackBar.showWhite(binding?.root, resource.data.message.uppercase())
                        } else if (resource.data?.code == -3) {
                            binding?.progressBar?.visibility = View.GONE
                            showDialogFragment(qrCode, resource.data.message)
                        }
                    }
                    is MyResource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        MySnackBar.showRed(binding?.root, resource.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showDialogFragment(qrCode: String, totalPenalty: String) {
        val bundle = Bundle()
        bundle.putString("qr_code", qrCode)
        bundle.putString("total_penalty", totalPenalty)
        activity?.supportFragmentManager?.let {
            val fragment = PaymentPenaltyFragment()
            fragment.arguments = bundle
            activity?.supportFragmentManager.let { fragmentManager ->
                if (fragmentManager != null) {
                    fragment.show(fragmentManager, "")
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