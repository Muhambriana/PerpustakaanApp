package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.databinding.ItemListBookBinding
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var listBook = mutableListOf<Book>()
    var onItemClick: ((Book) -> Unit)? = null

    fun setBookList(books: List<Book>?) {
        if (books == null) return
        listBook.clear()
        listBook.addAll(books)
        notifyDataSetChanged()
    }

    // For create every item in recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BookViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_book, parent, false))

    // For set data to view
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = listBook[position]
        holder.bind(book)
    }

    // To get size of book inside adapter
    override fun getItemCount(): Int {
        return listBook.size
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListBookBinding.bind(itemView)
        fun bind(book: Book){
            with(binding){
                tvBookTitle.text = book.title
            }
        }

        init {
            binding.root.setSingleClickListener {
                onItemClick?.invoke(listBook[adapterPosition])
            }
        }
    }
}

