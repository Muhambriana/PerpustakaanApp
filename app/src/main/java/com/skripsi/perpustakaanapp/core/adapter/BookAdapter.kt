package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.databinding.ItemListBookBinding
import com.skripsi.perpustakaanapp.utils.GlideManagement
import com.skripsi.perpustakaanapp.utils.NetworkInfo
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var listBook = mutableListOf<Book>()
    var onItemClick: ((Book) -> Unit)? = null

    private lateinit var glideManagement: GlideManagement

    fun setBookList(books: List<Book>?) {
        if (books == null) return
        listBook.clear()
        listBook.addAll(books)
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
                setBookPoster(book.imageUrl, itemView)
            }
        }

        private fun setBookPoster(avatar: String?, context: View) {
            if (avatar != null) {
                glideManagement = GlideManagement(context.context)
                Glide.with(context)
                    .load(NetworkInfo.BOOK_IMAGE_BASE_URL + avatar)
                    // For reload image on glide from the same url
                    .signature(ObjectKey(glideManagement.fetchCachePoster().toString()))
                    // To show the original size of image
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .fitCenter()
                    .into(binding.ivBookPoster)
            }
        }

        init {
            binding.root.setSingleClickListener {
                onItemClick?.invoke(listBook[adapterPosition])
            }
        }
    }
}

