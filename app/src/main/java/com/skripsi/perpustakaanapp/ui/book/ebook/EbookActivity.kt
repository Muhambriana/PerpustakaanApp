package com.skripsi.perpustakaanapp.ui.book.ebook

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import com.github.barteksc.pdfviewer.PDFView
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.databinding.ActivityEbookBinding
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class EbookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEbookBinding
//    private lateinit var pdfView: PDFView

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEbookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this@EbookActivity
//        pdfView = binding.viewPdf
        GetPDFromURl().execute("http://192.168.0.108:9080/api/library/image/eBook/show/EBook.pdf")
    }


    inner class GetPDFromURl(): AsyncTask<String, Void, InputStream>() {
        override fun onPreExecute() {
            super.onPreExecute()
            binding.progressBar.visibility = View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        override fun doInBackground(vararg p0: String?): InputStream? {
            var inputStream: InputStream? = null

            try {
                val url = URL(p0.get(0))

                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                if (urlConnection.responseCode == 200) {
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            } catch (e: Exception) {
                println("kocak error $e")
                e.printStackTrace()
                return null
            }
            return inputStream
        }

        override fun onPostExecute(result: InputStream?) {
            binding.viewPdf.fromStream(result).load()
            Handler(Looper.getMainLooper()).postDelayed({ binding.progressBar.visibility = View.GONE; window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);  }, 13000)
        }

    }
}