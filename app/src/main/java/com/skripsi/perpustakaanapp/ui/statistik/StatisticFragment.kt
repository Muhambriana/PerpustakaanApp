package com.skripsi.perpustakaanapp.ui.statistik

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.StatisticAdminModel
import com.skripsi.perpustakaanapp.core.models.StatisticMemberModel
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentStatisticBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog

class StatisticFragment : Fragment() {

    private lateinit var binding: FragmentStatisticBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: StatisticViewModel

    private val client = RetrofitClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStatisticBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstInitialization()
    }

    private fun firstInitialization() {
        sessionManager = SessionManager(requireContext())
        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            StatisticViewModel::class.java
        )

        getStatisticData()
    }

    private fun getStatisticData() {
        if (sessionManager.fetchUserRole() == "admin") {
            statisticAdmin()
        } else if (sessionManager.fetchUserRole() == "student") {
            statisticMember()
        }
    }

    private fun statisticAdmin() {
        viewModel.getStatisticAdmin(sessionManager.fetchAuthToken().toString())

        viewModel.resourceStatisticAdmin.observe(requireActivity()) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {}
                    is MyResource.Success -> {
                        displayDataAdmin(resource.data)
                    }
                    is MyResource.Error -> {
                        MyAlertDialog.show(requireContext(), R.drawable.icon_cancel, "Failed", resource.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun displayDataAdmin(data: StatisticAdminModel?) {
        binding.tvTitle1.text = "Peminjaman Yang Dimanaged"
        binding.tvTitle2.text = "Pengunjung"
        binding.tvTitle3.text = "Total Peminjaman"
        binding.tvTitle4.text = "Total Dikembalikan"
        binding.tvStat1.text = data?.managedBook.toString()
        binding.tvStat2.text = data?.visitors.toString()
        binding.tvStat3.text = data?.transaction.toString()
        binding.tvStat4.text = data?.returnedBook.toString()
    }

    private fun statisticMember() {
        viewModel.getStatisticMember(sessionManager.fetchAuthToken().toString())

        viewModel.resourceStatisticMember.observe(requireActivity()) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {}
                    is MyResource.Success -> {
                        displayDataMember(resource.data)
                    }
                    is MyResource.Error -> {
                        MyAlertDialog.show(requireContext(), R.drawable.icon_cancel, "Failed", resource.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun displayDataMember(data: StatisticMemberModel?) {
        binding.tvTitle1.text = "Belum Dikembalikan"
        binding.tvTitle2.text = "Pengejauan Ditolak"
        binding.tvTitle3.text = "Melewati Batas Waktu"
        binding.tvTitle4.text = "Total Peminjaman"
        binding.tvStat1.text = data?.ongoingTransaction.toString()
        binding.tvStat2.text = data?.rejectedTransaction.toString()
        binding.tvStat3.text = data?.overdueTransaction.toString()
        binding.tvStat4.text = data?.history.toString()
    }
}