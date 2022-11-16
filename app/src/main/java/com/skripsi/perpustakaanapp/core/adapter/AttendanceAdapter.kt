package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.Attendance
import com.skripsi.perpustakaanapp.databinding.ItemListAttendanceBinding

class AttendanceAdapter: RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    private var listAttendance = mutableListOf<Attendance>()
    var onItemClick: ((Attendance) -> Unit)? = null

    fun setAttendanceList(attendances: List<Attendance>?) {
        if (attendances == null) return
        listAttendance.clear()
        listAttendance.addAll(attendances)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AttendanceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_attendance, parent,false))

    override fun onBindViewHolder(holder: AttendanceAdapter.AttendanceViewHolder, position: Int) {
        val attendance = listAttendance[position]
        holder.bind(attendance)
    }

    override fun getItemCount(): Int {
        return listAttendance.size
    }

    inner class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListAttendanceBinding.bind(itemView)
        fun bind(attendance: Attendance) {
            with(binding) {
                tvItemUsername.text = attendance.user
                tvItemOfficer.text = attendance.admin
                tvItemDate.text = attendance.date
                tvItemClockIn.text = attendance.clockIn
                tvItemClockOut.text = attendance.clockOut
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listAttendance[adapterPosition])
            }
        }
    }

}