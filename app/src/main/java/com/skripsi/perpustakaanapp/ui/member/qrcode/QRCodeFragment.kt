package com.skripsi.perpustakaanapp.ui.member.qrcode

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.databinding.FragmentQRCodeBinding
import com.skripsi.perpustakaanapp.utils.GenerateQRCode

// TODO: Rename parameter arguments, choose names that match
class QRCodeFragment : Fragment() {

    private lateinit var sessionManager: SessionManager

    private var fragmentQRCodeBinding: FragmentQRCodeBinding? = null
    private val binding get() = fragmentQRCodeBinding
    private var qrCode: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentQRCodeBinding = FragmentQRCodeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getQR()
        val result:Bitmap? = GenerateQRCode.generate(qrCode)
        if (result != null) {
            binding?.qrCodeImage?.setImageBitmap(result)
        }
    }


    private fun getQR() {
        sessionManager= SessionManager(requireContext())
        val bundle = arguments?.getString("qr_code")
        if (bundle != null) {
            qrCode = bundle
        } else if (sessionManager.fetchQRCode() != null) {
            qrCode = sessionManager.fetchQRCode()
        }
    }
}