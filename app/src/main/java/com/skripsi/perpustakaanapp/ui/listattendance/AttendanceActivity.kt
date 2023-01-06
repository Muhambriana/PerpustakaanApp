package com.skripsi.perpustakaanapp.ui.listattendance

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.AttendanceAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Attendance
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityAttendanceBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog

class AttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: AttendanceViewModel

    private val client = RetrofitClient
    private val attendanceAdapter = AttendanceAdapter()
    private var attendanceData : List<Attendance>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        getAttendanceData()
    }

    private fun firstInitialization() {
        supportActionBar?.title = "History Kunjungan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            AttendanceViewModel::class.java
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    
    private fun getAttendanceData() {
        setRequest()

        viewModel.resourceAttendance.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        attendanceData = resource.data
                        showRecycleList()
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showWith2Event(this, R.drawable.icon_cancel, resource.message.toString(), resources.getString(
                            R.string.refresh), resources.getString(R.string.back_to_dashboard),
                            { _, _ ->
                                getAttendanceData()
                            },
                            { _,_ ->
                                finish()
                            }
                        )
                    }
                    is MyResource.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewEmpty.root.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setRequest() {
        if (sessionManager.fetchUserRole() == "admin") {
            viewModel.getAllAttendances(sessionManager.fetchAuthToken().toString())
        } else if (sessionManager.fetchUserRole() == "student") {
            viewModel.getAllAttendanceMember(sessionManager.fetchAuthToken().toString())
        }
    }

    private fun showRecycleList() {
        binding.rvAttendance.layoutManager = LinearLayoutManager(this)
        binding.rvAttendance.adapter = attendanceAdapter
        attendanceAdapter.setAttendanceList(attendanceData)

        // On user item click
    }
}