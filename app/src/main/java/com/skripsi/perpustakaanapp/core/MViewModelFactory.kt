package com.skripsi.perpustakaanapp.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.ui.admin.createbook.CreateBookViewModel
import com.skripsi.perpustakaanapp.ui.admin.createnewadmin.CreateNewAdminViewModel
import com.skripsi.perpustakaanapp.ui.admin.pendingtask.PendingTaskViewModel
import com.skripsi.perpustakaanapp.ui.home.HomeViewModel
import com.skripsi.perpustakaanapp.ui.login.LoginViewModel
import com.skripsi.perpustakaanapp.ui.register.RegisterViewModel
import com.skripsi.perpustakaanapp.ui.user.book.BookViewModel
import com.skripsi.perpustakaanapp.ui.user.loan.LoanViewModel

class MViewModelFactory constructor(private val libraryRepository: LibraryRepository) :
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
            modelClass.isAssignableFrom(PendingTaskViewModel::class.java) -> {
                return PendingTaskViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(LoanViewModel::class.java) -> {
                return  LoanViewModel(this.libraryRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }


    }
}