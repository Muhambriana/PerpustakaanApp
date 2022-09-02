package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.PendingLoan
import com.skripsi.perpustakaanapp.databinding.ItemListPendingLoanBinding
import com.skripsi.perpustakaanapp.ui.setSingleClickListener

class PendingLoanAdapter : RecyclerView.Adapter<PendingLoanAdapter.PendingLoanViewHolder>() {

    private var listPendingLoan = mutableListOf<PendingLoan>()
    var onItemClick: ((PendingLoan) -> Unit)? = null
    var buttonApproveClick: ((Int) -> Unit)? = null
    var buttonRejectClick: ((Int) -> Unit)? = null
    var onMemberUsernameClick: ((String) -> Unit)? = null
    var onBookTitleClick: ((String) -> Unit)? = null

    fun setPendingLoanList(pendingLoanData: List<PendingLoan>?) {
        if (pendingLoanData == null) return
        listPendingLoan.clear()
        listPendingLoan.addAll(pendingLoanData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PendingLoanViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_pending_loan, parent, false))


    override fun onBindViewHolder(holder: PendingLoanViewHolder, position: Int) {
        val pendingLoan = listPendingLoan[position]
        holder.bind(pendingLoan)
    }

    override fun getItemCount(): Int {
        return listPendingLoan.size
    }

    inner class PendingLoanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListPendingLoanBinding.bind(itemView)
        fun bind(pendingLoan: PendingLoan) {
            with(binding){
                tvBookTitle.text = pendingLoan.bookTitle
                tvMember.text = pendingLoan.createdBy
            }
        }

        init {
            binding.btnApprove.setSingleClickListener {
                listPendingLoan[adapterPosition].pendingLoanId?.let { id ->
                    buttonApproveClick?.invoke(id)
                }
            }
            binding.btnReject.setSingleClickListener {
                listPendingLoan[adapterPosition].pendingLoanId?.let { id ->
                    buttonRejectClick?.invoke(id)
                }
            }
            binding.tvMember.setSingleClickListener {
                listPendingLoan[adapterPosition].createdBy?.let { memberUsername ->
                    onMemberUsernameClick?.invoke(memberUsername)
                }
            }
            binding.tvBookTitle.setSingleClickListener {
                listPendingLoan[adapterPosition].bookId?.let { bookId ->
                    onBookTitleClick?.invoke(bookId)
                }
            }
        }
    }

}