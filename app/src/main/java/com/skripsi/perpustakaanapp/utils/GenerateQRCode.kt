package com.skripsi.perpustakaanapp.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.skripsi.perpustakaanapp.utils.GenerateQRCode.getQRCode

object GenerateQRCode {
    private fun getQRCode(text: String?): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400)

        val w = bitMatrix.width
        val h = bitMatrix.height
        val pixels = IntArray(w * h)
        for (y in 0 until h ) {
            for (x in 0 until w){
                pixels[y * w + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }

        val qrCode = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        qrCode.setPixels(pixels, 0, w, 0, 0, w, h)
        return qrCode
    }

    fun generate(username: String?): Bitmap {
        return getQRCode(username)
    }
}