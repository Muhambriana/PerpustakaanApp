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
        val result:Bitmap? = generateQRCode()
        if (result != null) {
            binding?.qrCodeImage?.setImageBitmap(result)
        }
    }

    private fun generateQRCode(): Bitmap? {
        sessionManager = SessionManager(requireContext())
        getQR()
        var bitmap: Bitmap? = null
        if (qrCode != null) {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, 400, 400)

            val w = bitMatrix.width
            val h = bitMatrix.height
            val pixels = IntArray(w * h)
            for (y in 0 until h ) {
                for (x in 0 until w){
                    pixels[y * w + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                }
            }

            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        }
        return bitmap
    }

    private fun getQR() {
        val bundle = arguments?.getString("qr_code")
        if (bundle != null) {
            qrCode = bundle
        } else if (sessionManager.fetchQRCode() != null) {
            qrCode = sessionManager.fetchQRCode()
        }
    }
}