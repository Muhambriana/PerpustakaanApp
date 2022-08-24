package com.skripsi.perpustakaanapp.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook.CreateBookViewModel
import com.skripsi.perpustakaanapp.ui.admin.createnewadmin.CreateNewAdminViewModel
import com.skripsi.perpustakaanapp.ui.admin.loanmanagerial.pendingloan.PendingLoanViewModel
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook.UpdateBookViewModel
import com.skripsi.perpustakaanapp.ui.home.HomeViewModel
import com.skripsi.perpustakaanapp.ui.login.LoginViewModel
import com.skripsi.perpustakaanapp.ui.member.register.RegisterViewModel
import com.skripsi.perpustakaanapp.ui.book.listbook.BookViewModel
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookViewModel
import com.skripsi.perpustakaanapp.ui.member.loanhistory.LoanHistoryViewModel

class MyViewModelFactory constructor(private val libraryRepository: LibraryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                return RegisterViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(BookViewModel::class.java) -> {
                return BookViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(CreateBookViewModel::class.java) -> {
                return  CreateBookViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(CreateNewAdminViewModel::class.java) -> {
                return  CreateNewAdminViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(PendingLoanViewModel::class.java) -> {
                return PendingLoanViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(LoanHistoryViewModel::class.java) -> {
                return  LoanHistoryViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(UpdateBookViewModel::class.java) -> {
                return UpdateBookViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(DetailBookViewModel::class.java) -> {
                return DetailBookViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(PendingLoanViewModel::class.java) -> {
                return PendingLoanViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(LoanHistoryViewModel::class.java) -> {
                return LoanHistoryViewModel(this.libraryRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }


    }
}