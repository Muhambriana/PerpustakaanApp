package com.skripsi.perpustakaanapp.ui.book.detailloanhistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.LoanHistory
import com.skripsi.perpustakaanapp.databinding.ActivityDetailLoanBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.ViewImageFragment
import com.skripsi.perpustakaanapp.ui.admin.scanner.PaymentPenaltyFragment
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity
import com.skripsi.perpustakaanapp.ui.member.qrcode.QRCodeBottomFragment
import com.skripsi.perpustakaanapp.ui.member.qrcode.QRCodeFragment
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity
import com.skripsi.perpustakaanapp.utils.GenerateQRCode
import com.skripsi.perpustakaanapp.utils.NetworkInfo
import com.skripsi.perpustakaanapp.utils.NetworkInfo.BOOK_IMAGE_BASE_URL
import jp.wasabeef.glide.transformations.BlurTransformation

class DetailLoanHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLoanBinding
    private lateinit var sessionManager: SessionManager

    private var detailLoanHistory: LoanHistory? = null
    private val client = RetrofitClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLoanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
    }

    private fun firstInitialization() {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.color_actionbar_background))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionManager = SessionManager(this)
        setDataToModels()
    }

    private fun setDataToModels() {
        detailLoanHistory = intent.getParcelableExtra(EXTRA_DATA)
        showDetailLoan()
    }

    private fun showDetailLoan() {
        binding.progressBar.visibility = View.GONE
        binding.tvDetailStatus.text = detailLoanHistory?.status
        binding.tvDetailStatus.background = setStatusColor(detailLoanHistory?.status)?.let { ContextCompat.getDrawable(this, it) }
        binding.tvDateProposedDetail.text = detailLoanHistory?.proposedDate
        binding.tvDetailBookTitle.text = detailLoanHistory?.bookTitle
        binding.tvDetailMember.text = detailLoanHistory?.borrower
        binding.tvDetailAdmin.text = detailLoanHistory?.managedBy
        binding.tvDetailPenalty.text = detailLoanHistory?.penalty.toString()
        binding.tvDetailReleaseDate.text = detailLoanHistory?.releaseDate
        binding.tvDetailDueDate.text = detailLoanHistory?.dueDate
        binding.tvDetailReturnDate.text = detailLoanHistory?.returnDate
        setBookPoster(detailLoanHistory?.bookPoster)
        setQR()
        onClickListener()
        baseOnRole()
    }

    private fun setBookPoster(poster: String?) {
        if (poster != null) {
            glideSetup(poster)
        }
    }

    private fun glideSetup(imageName: String?) {
        val imageUrl = GlideUrl("$BOOK_IMAGE_BASE_URL$imageName/${System.currentTimeMillis()}") { mapOf(Pair("Authorization", sessionManager.fetchAuthToken())) }

        Glide.with(this)
            .load(imageUrl)
            // To show the original size of image
            .override(200, 600)
            .fitCenter()
            .into(binding.ivDetailBookPoster)
    }

    private fun setQR() {
        binding.imgDetailQr.setImageBitmap(GenerateQRCode.generate(detailLoanHistory?.qrCode))
    }

    private fun onClickListener() {
        // On Item book click
        binding.cvBook.setOnClickListener {
            val intent = Intent(this, DetailBookActivity::class.java)
            intent.putExtra(DetailBookActivity.BOOK_ID, detailLoanHistory?.bookId)
            startActivity(intent)
        }

        // On Username click
        binding.tvDetailMember.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.USERNAME, detailLoanHistory?.borrower)
            startActivity(intent)
        }

        binding.imgDetailQr.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("qr_code", detailLoanHistory?.qrCode)
            val fragment = QRCodeBottomFragment()
            fragment.arguments =  bundle
            fragment.show(supportFragmentManager, "ViewImageFragment")
        }
    }

    private fun baseOnRole() {
        if (sessionManager.fetchUserRole() == "admin") {
            Glide.with(this)
                .load(GenerateQRCode.generate(detailLoanHistory?.qrCode))
                .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                .into(binding.imgDetailQr)

            binding.imgDetailQr.isClickable = false
        }
    }


    private fun setStatusColor(status: String?): Int? {
        if (null == status) {
            return null
        }
        if (status == "RETURNED") {
            return R.drawable.custom_blue_text_view_background
        }
        if (status == "APPROVED") {
            return R.drawable.custom_green_text_view_background
        }
        if (status == "REJECT") {
            return R.drawable.custom_red_text_view_background
        }
        if (status == "PENDING") {
            return R.drawable.custom_yellow_text_view_background
        }
        return null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}