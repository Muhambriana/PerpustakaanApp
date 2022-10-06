package com.skripsi.perpustakaanapp.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.qrCodeImage.setImageBitmap(generateQRCode())
    }


    private fun generateQRCode(): Bitmap? {
        sessionManager = SessionManager(this)
        var bitmap: Bitmap? = null
        if (sessionManager.fetchQRCode() != null) {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(sessionManager.fetchQRCode(), BarcodeFormat.QR_CODE, 400, 400)

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



}