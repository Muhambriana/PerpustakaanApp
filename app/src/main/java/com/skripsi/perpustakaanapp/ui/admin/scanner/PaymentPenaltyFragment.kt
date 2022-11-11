package com.skripsi.perpustakaanapp.ui.admin.scanner

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentPaymentPenaltyBinding
import com.skripsi.perpustakaanapp.databinding.FragmentScannerReturningBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import me.dm7.barcodescanner.zxing.ZXingScannerView

class PaymentPenaltyFragment : DialogFragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ScannerReturnBookViewModel

    private var fragmentPaymentPenaltyBinding: FragmentPaymentPenaltyBinding? = null
    private val binding get() = fragmentPaymentPenaltyBinding
    private val client = RetrofitClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentPaymentPenaltyBinding = FragmentPaymentPenaltyBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstInitialization()
    }

    private fun firstInitialization() {
        binding?.progressBar?.visibility = View.INVISIBLE

        sessionManager = SessionManager(requireContext())

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            ScannerReturnBookViewModel::class.java
        )

        val qrCode = arguments?.getString("qr_code")
        if (qrCode == null) {
            binding?.upload?.isEnabled = false
            return
        }
        binding?.upload?.setOnClickListener {
            MyAlertDialog.showWith2Event(
                requireContext(),
                null,
                resources.getString(R.string.penalty_confirmation),
                resources.getString(R.string.confirmation_yes),
                resources.getString(R.string.confirmation_recheck),
                {_,_ ->
                    postReturnBook(qrCode)
                }, {_,_ ->

                })

        }
        binding?.tvPenalty?.text = arguments?.getString("total_penalty")
    }

    private fun postReturnBook(qrCode: String) {

        viewModel.scannerOverdueReturning(sessionManager.fetchAuthToken().toString(), qrCode, binding?.edtMoney?.text.toString().toBigDecimalOrNull())

        viewModel.resourcePenaltyPayment.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        if (resource.data?.code == 0) {
                            binding?.progressBar?.visibility = View.GONE
                            binding?.upload?.isEnabled = false
                            MySnackBar.showWhite(binding?.root, resource.data.message.uppercase())
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    dismiss()
                                }, 1500)
                        } else if (resource.data?.code == -3) {
                            binding?.progressBar?.visibility = View.GONE
                            MySnackBar.showRed(binding?.root, "Pastikan Jumlah Pembayaran Sesuai Dengan Denda")
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
}