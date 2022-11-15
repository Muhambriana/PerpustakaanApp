package com.skripsi.perpustakaanapp.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.bookcategory.BookCategoryViewModel
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook.CreateBookViewModel
import com.skripsi.perpustakaanapp.ui.admin.scanner.ScannerReturnBookViewModel
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook.UpdateBookViewModel
import com.skripsi.perpustakaanapp.ui.admin.listuser.UserViewModel
import com.skripsi.perpustakaanapp.ui.admin.pendingloan.PendingLoanAdminViewModel
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.createnewadmin.CreateNewAdminViewModel
import com.skripsi.perpustakaanapp.ui.admin.scanner.ScannerAttendanceViewModel
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.updateuser.UpdateUserViewModel
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookViewModel
import com.skripsi.perpustakaanapp.ui.book.ebook.EbookViewModel
import com.skripsi.perpustakaanapp.ui.book.listbook.BookViewModel
import com.skripsi.perpustakaanapp.ui.home.HomeViewModel
import com.skripsi.perpustakaanapp.ui.loan.AdminLoanViewModel
import com.skripsi.perpustakaanapp.ui.loan.MemberLoanViewModel
import com.skripsi.perpustakaanapp.ui.login.LoginViewModel
import com.skripsi.perpustakaanapp.ui.member.favoritebook.FavoriteBookViewModel
import com.skripsi.perpustakaanapp.ui.listattendance.AttendanceViewModel
import com.skripsi.perpustakaanapp.ui.member.loanhistory.LoanHistoryViewModel
import com.skripsi.perpustakaanapp.ui.member.register.RegisterViewModel
import com.skripsi.perpustakaanapp.ui.statistik.StatisticViewModel
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileViewModel

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
            modelClass.isAssignableFrom(PendingLoanAdminViewModel::class.java) -> {
                return PendingLoanAdminViewModel(this.libraryRepository) as T
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
            modelClass.isAssignableFrom(PendingLoanAdminViewModel::class.java) -> {
                return PendingLoanAdminViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(LoanHistoryViewModel::class.java) -> {
                return LoanHistoryViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(UserProfileViewModel::class.java) -> {
                return UserProfileViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteBookViewModel::class.java) -> {
                return FavoriteBookViewModel(this.libraryRepository)  as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                return UserViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(UpdateUserViewModel::class.java) -> {
                return UpdateUserViewModel(this.libraryRepository)  as T
            }
            modelClass.isAssignableFrom(ScannerAttendanceViewModel::class.java) -> {
                return ScannerAttendanceViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(AttendanceViewModel::class.java) -> {
                return AttendanceViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(ScannerReturnBookViewModel::class.java) -> {
                return ScannerReturnBookViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(EbookViewModel::class.java) -> {
                return EbookViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(MemberLoanViewModel::class.java) -> {
                return MemberLoanViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(AdminLoanViewModel::class.java) -> {
                return AdminLoanViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(StatisticViewModel::class.java) -> {
                return StatisticViewModel(this.libraryRepository) as T
            }
            modelClass.isAssignableFrom(BookCategoryViewModel::class.java) -> {
                return BookCategoryViewModel(this.libraryRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }


    }
}