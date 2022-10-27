package com.skripsi.perpustakaanapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.databinding.FragmentViewImageBinding
import com.skripsi.perpustakaanapp.utils.NetworkInfo
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class ViewImageFragment : DialogFragment() {

    private var fragmentViewImageBinding:FragmentViewImageBinding? = null
    private val binding get() = fragmentViewImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentViewImageBinding = FragmentViewImageBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setImageToView()
        buttonCloseListener()
    }

    private fun buttonCloseListener() {
        binding?.buttonClose?.setSingleClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)
                ?.commit()
        }
    }

    private fun setImageToView() {
        val imageUrl = arguments?.getString("imageUrl")
        binding?.let {
            Glide.with(this)
                .load(NetworkInfo.BOOK_IMAGE_BASE_URL + imageUrl)
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(it.imageView)
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }



}