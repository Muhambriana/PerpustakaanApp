package com.skripsi.perpustakaanapp.ui.statistik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skripsi.perpustakaanapp.R

class MemberStatisticFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}