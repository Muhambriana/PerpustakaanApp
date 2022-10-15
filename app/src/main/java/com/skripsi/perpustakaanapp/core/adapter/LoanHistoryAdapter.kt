package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.LoanHistory
import com.skripsi.perpustakaanapp.databinding.ItemListLoanHistoryBinding
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class LoanHistoryAdapter : RecyclerView.Adapter<LoanHistoryAdapter.LoanHistoryViewHolder>() {

    private val listLoanHistory = mutableListOf<LoanHistory>()
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
                tvBookTitle.text = loanHistory.bookTitle
                tvOfficer.text = loanHistory.managedBy
            }
        }

        init {
            binding.tvBookTitle.setSingleClickListener {
                listLoanHistory[adapterPosition].bookId?.let { bookId ->
                    onBookTitleClick?.invoke(bookId) }
            }
            binding.tvOfficer.setSingleClickListener {
                listLoanHistory[adapterPosition].managedBy?.let { officerUsername ->
                    onOfficerUsernameClick?.invoke(officerUsername)
                }
            }
        }
    }

}