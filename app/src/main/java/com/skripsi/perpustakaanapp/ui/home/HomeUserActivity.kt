package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.databinding.ActivityHomeUserBinding
import com.skripsi.perpustakaanapp.ui.admin.createbook.CreateBookActivity
import com.skripsi.perpustakaanapp.ui.user.book.BookActivity
import com.skripsi.perpustakaanapp.ui.user.loan.LoanActivity


class HomeUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Daya Utama Library"

        if (intent.extras!=null){
            println(intent.getStringExtra("user_name"))
            binding.userName.text = intent.getStringExtra("user_name")
        }

        cardListener()
//        list.addAll(MenuResource.listResource)
//        menuAdapter =MenuAdapter(list)
//        setUpRecyclerView()
    }

    private fun cardListener() {
        val clickListener = View.OnClickListener {view ->
            when (view.id){
                R.id.book_list -> {
                    val intent = Intent(this@HomeUserActivity, BookActivity::class.java)
                    startActivity(intent)
                }
                R.id.loan_list -> {
                    val intent = Intent(this@HomeUserActivity, LoanActivity::class.java)
                    startActivity(intent)
                }
            }

        }

        binding.bookList.setOnClickListener(clickListener)
        binding.loanList.setOnClickListener(clickListener)
    }

//    override fun onClick(v: View?) {
//        println("masuk bos")
//        when (v?.id) {
//            R.id.book_list -> {
//                val intent = Intent(this@HomeActivity, BookActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.loan_list -> {
//                val intent = Intent(this@HomeActivity, LoanActivity::class.java)
//                startActivity(intent)
//            }
//        }
//    }

    private fun setUpRecyclerView() {
//        binding.rvMenu.apply {
//            layoutManager = GridLayoutManager(context, 2)
//            adapter = menuAdapter
//            setHasFixedSize(true)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//        }
    }
}