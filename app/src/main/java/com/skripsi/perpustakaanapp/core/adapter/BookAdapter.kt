package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.databinding.ItemListBookBinding
import com.skripsi.perpustakaanapp.utils.NetworkInfo.BOOK_IMAGE_BASE_URL
import com.skripsi.perpustakaanapp.utils.setSingleClickListener
import java.util.*

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var listBook = mutableListOf<Book>()
    var onItemClick: ((Book) -> Unit)? = null

    private lateinit var sessionManager: SessionManager

    fun setBookList(books: List<Book>?) {
        if (books == null) return
        listBook.clear()
        listBook.addAll(books)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        sessionManager = SessionManager(recyclerView.context)
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
                tvBookAuthor.text = book.author
                tvBookCategory.text = book.category
                setBookPoster(book.imageUrl)
            }
        }

        private fun setBookPoster(poster: String?) {
            if (poster == null || poster == "null") {
                binding.ivBookPoster.setImageResource(R.color.teal_200)
                return
            }
            glideSetup(poster)
        }

        private fun glideSetup(imageName: String?) {
            val randomString = UUID.randomUUID().toString()
            val imageUrl = GlideUrl("$BOOK_IMAGE_BASE_URL$imageName/?$randomString") { mapOf(Pair("Authorization", sessionManager.fetchAuthToken())) }

            Glide.with(itemView)
                .load(imageUrl)
                // To show the original size of image
                .override(200, 600)
                .fitCenter()
                .into(binding.ivBookPoster)
        }

        init {
            binding.root.setSingleClickListener {
                onItemClick?.invoke(listBook[adapterPosition])
            }
        }
    }
}

