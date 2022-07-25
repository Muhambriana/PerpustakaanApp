package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
//    private var list: ArrayList<Sibi> = arrayListOf()
//    private lateinit var sibiAdapter: SibiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(inflater.context), container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        list.addAll(SibiData.listData)
//        sibiAdapter = SibiAdapter(list)
//        setUpRecyclerView()
    }

//    private fun setUpRecyclerView() {
//        binding?.rvSibi?.apply {
//            layoutManager = GridLayoutManager(context,2)
//            adapter = sibiAdapter
//            setHasFixedSize(true)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}