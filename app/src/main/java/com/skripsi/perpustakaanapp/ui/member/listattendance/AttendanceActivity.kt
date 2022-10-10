package com.skripsi.perpustakaanapp.ui.member.listattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.AttendanceAdapter
import com.skripsi.perpustakaanapp.core.adapter.UserAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Attendance
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityAttendanceBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.home.HomeViewModel

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
        supportActionBar?.title = "Attendance"
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
        viewModel.getAllAttendances(sessionManager.fetchAuthToken().toString())

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
                }
            }
        }
    }

    private fun showRecycleList() {
        binding.rvHistoryLoan.layoutManager = LinearLayoutManager(this)
        binding.rvHistoryLoan.adapter = attendanceAdapter
        attendanceAdapter.setAttendanceList(attendanceData)

        // On user item click
    }
}