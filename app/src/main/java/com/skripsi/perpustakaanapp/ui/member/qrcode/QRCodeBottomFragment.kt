package com.skripsi.perpustakaanapp.ui.member.qrcode

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skripsi.perpustakaanapp.databinding.FragmentQRCodeBottomBinding
import com.skripsi.perpustakaanapp.utils.GenerateQRCode
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class QRCodeBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentQRCodeBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentQRCodeBottomBinding.inflate(LayoutInflater.from(inflater.context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showQRCode()
        setButtonListener()
    }

    private fun setButtonListener() {
        binding.buttonBack.setSingleClickListener {
            dismiss()
        }
    }


    private fun showQRCode() {
        val bundle = arguments?.getString("qr_code")
        if (bundle != null) {
            val result: Bitmap? = GenerateQRCode.generate(bundle)
            if (result != null) {
                binding.qrCodeImage.setImageBitmap(result)
            }
        }
    }
}