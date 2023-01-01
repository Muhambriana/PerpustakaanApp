package com.skripsi.perpustakaanapp.core.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.LoanHistory
import com.skripsi.perpustakaanapp.databinding.ItemListLoanHistoryBinding
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class LoanHistoryAdapter : RecyclerView.Adapter<LoanHistoryAdapter.LoanHistoryViewHolder>() {

    private val listLoanHistory = mutableListOf<LoanHistory>()
    var onItemClick: ((LoanHistory) -> Unit)? = null
    var onBookTitleClick : ((String) -> Unit)? = null
    var onOfficerUsernameClick : ((String) -> Unit)? = null

    fun setLoanHistoryList(loanHistoryData: List<LoanHistory>?) {
        if (loanHistoryData == null) return
        listLoanHistory.clear()
        listLoanHistory.addAll(loanHistoryData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LoanHistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_loan_history, parent, false))

    override fun onBindViewHolder(holder: LoanHistoryViewHolder, position: Int) {
        val loanHistory = listLoanHistory[position]
        holder.bind(loanHistory)
    }

    override fun getItemCount(): Int {
        return listLoanHistory.size
    }

    inner class LoanHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListLoanHistoryBinding.bind(itemView)
        fun bind(loanHistory: LoanHistory) {
            with(binding) {
                tvItemBookTitle.text = loanHistory.bookTitle
                tvItemOfficer.text = loanHistory.managedBy
                tvItemBorrower.text = loanHistory.borrower
                tvItemStatus.text = loanHistory.status
                tvItemStatus.background = setStatusColor(loanHistory.status)?.let {
                    ContextCompat.getDrawable(itemView.context,
                        it)
                }
            }
        }

        init {
            binding.root.setSingleClickListener {
                onItemClick?.invoke(listLoanHistory[adapterPosition])
            }
            binding.tvItemBookTitle.setSingleClickListener {
                listLoanHistory[adapterPosition].bookId?.let { bookId ->
                    onBookTitleClick?.invoke(bookId) }
            }
            binding.tvItemOfficer.setSingleClickListener {
                listLoanHistory[adapterPosition].managedBy?.let { officerUsername ->
                    onOfficerUsernameClick?.invoke(officerUsername)
                }
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
    }

}