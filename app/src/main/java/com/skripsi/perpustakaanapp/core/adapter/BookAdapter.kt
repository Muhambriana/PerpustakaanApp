package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.databinding.ItemListBookBinding

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var listBook = mutableListOf<Book>()
    var onItemClick: ((Book) -> Unit)? = null
//    var positionAdapter: Int? = null

    fun setBookList(books: List<Book>?) {
        if (books == null) return
        listBook.clear()
        listBook.addAll(books)
        notifyDataSetChanged()
    }

    // untuk membuat setiap item recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BookViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_book, parent, false))

    // untuk memasukkan atau set data ke dalam view
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = listBook[position]
        holder.bind(book)
    }

    // untuk mendapatkan jumlah data buku yang dimasukkan ke dalam adapter
    override fun getItemCount(): Int {
        return listBook.size
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListBookBinding.bind(itemView)
        fun bind(book: Book){
            with(binding){
                tvBookTitle.text = book.title
//                positionAdapter = adapterPosition
//                Glide.with(holder.itemView.context)
//                    .load(book.poster)
//                    .placeholder(R.drawable.placeholder)
//                    .into(holder.binding.moviePoster)

            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listBook[adapterPosition])
            }

//            binding.buttonMore.setOnClickListener{
//                onItemClick?.invoke(books[adapterPosition])
//            }
        }
    }
}

