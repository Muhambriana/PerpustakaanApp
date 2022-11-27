package com.skripsi.perpustakaanapp.core.adapter

class TrashClass {
    fun main() {
        //===LOGIN===
//        Start
//        Tampil splash screen
//        Tampil halaman login
//        Tampil form login
//        Input username dan password
//                If username dan password == database
//                    API membuat token dan menyimpan di response body
//                    Mengisi Response body dengan data user
//                    Return response body
//                Else
//                    Tetap di halaman login 
//                    Muncul pesan gagal
//                End if 
//        End




        //===Tambah buku baru===
//        Start
//        Tampil halaman dashboard
//        Dipilih menu tambah buku
//        Tampil halaman tambah buku 
//        Isi form buku yang tersedia
//        Pilih kategori buku
//        Pilih gambar untuk poster buku
//        Pilih pdf untuk ebook 
//        If klik simpan then 
//            Jalankan fungsi askAppointment()
//            If field judul buku  != null && stock buku != null && kategori != null then
//                Jalankan fungsi postBookData()
//            Else
//                Tampil pesan error
//            End if
//        Else
//            Kembali ke halaman dashboard
//        End if
//        End





        //=== Tambah admin baru===
//        Start
//        Tampil halaman dashboard
//        Dipilih menu tambah admin
//        Tampil halaman tambah tambah admin
//        Isi form user yang tersedia
//        Pilih jenjang
//        Pilih jenis kelamin
//        If klik simpan then
//        Jalankan fungsi askAppointment()
//        If field username  != null && password != null && jenis kelamin != null && jenjang != null then
//                Jalankan fungsi postUserData()
//        Else
//        Tampil pesan error
//        End if
//            Else
//        Kembali ke halaman dashboard
//        End


//
//        //=== Daftar buku===
//        Start
//        Tampil halaman dashboard
//        Dipilih menu daftar buku
//        Hide menu favorite
//        Dijalankan fungsi getBookData()
//        If data buku != empty then
//            Tampilkan list buku
//            If klik salah satu item buku then
//                Tampilkan detail buku
//            Else
//                Tetap di halaman daftar buku
//            End if
//        Else
//            Tampilkan pesan data kosong
//        End if
//        End
//
//        //=== Daftar user===
//        Start
//        Tampil halaman dashboard
//        Dipilih menu daftar buku
//        Dijalankan fungsi getUserData()
//        If list buku != empty then
//            Tampilkan list user
//            If klik salah satu item user then
//                Tampilkan detail user
//            Else
//                Tetap di halaman daftar user
//            End if
//        Else
//            Tampilkan pesan data kosong
//        End if
//        End
//
//        //===Pending loan===
//        Start
//        Tampil halaman dashboard
//        Dipilih menu menunggu persetujuan
//        Dijalankan fungsi getPendingLoanData()
//        If list user != empty then
//            Tampilkan list peminjaman tertunda
//            If klik button ceklis then
//                Jalankan fungsi postApproveLoan()
//            If klik button silang then
//                Jalankan fungsi postRejectLoan()
//            Else
//                Tetap di halaman menunggu persutujuan
//            End if
//        Else
//            Tampilkan pesan data kosong
//        End if
//        End
//
//
//        //===ON LOAN===
//        Start
//        Tampil halaman dashboard
//        Dipilih menu sedang dipinjam
//        Dijalankan fungsi getLoanData()
//        Dijalankan fungsi setRequest()
//        Dijalankan fungsi viewModel getAllOngoingLoan()
//        If list peminjaman != empty then
//            Tampilkan list peminjaman sedang berlangsung
//            if klik salah satu item peminjaman then
//                Tampilkan detail peminjaman
//            Else
//                Tetap di halaman sedang dipinjam
//        Else
//            Tampilkan pesan data kosong
//        End if
//        End
//
//
//
//
//        //===HISTORY LOAN===
//        Start
//        Tampil halaman dashboard
//        Dipilih menu sedang dipinjam
//        Dijalankan fungsi getLoanData()
//        Dijalankan fungsi setRequest()
//        Dijalankan fungsi viewModel getAllFinishLoan()
//        If list history != empty then
//            Tampilkan list peminjaman sedang berlangsung
//            If klik salah satu item history peminjaman then
//                Tampilkan detail peminjaman
//            Else
//                Tetap di halaman history peminjaman
//        Else
//            Tampilkan pesan data kosong
//        End if
//        End
//
//        //=== Daftar Absen Kunjungan===
//        Start
//        Tampil halaman dashboard
//        Dipilih menu sedang dipinjam
//        Dijalankan fungsi getAttendanceData()
//        Dijalankan fungsi setRequest()
//        Dijalankan fungsi viewModel getAllAttendance()
//        If list absen != empty then
//            Tampilkan list absen
//        Else
//            Tampilkan pesan data kosong
//        End if
//        End
//
//
//
//
//        //===SCAN KEHADIRAN===
//        Start
//        Tampil halaman dashboard
//        Dipilih menu scan pengunjung
//        If permission check == true
//            Buka kamera
//            Jalankan fungsi readQR()
//            If qr tidak terbaca
//            Kembali ke baris 6
//            End if
//        Else
//            Meminta user menyetujui persyaratan
//        End if
//        End
//
//        //===SCAN PENGEMBALIAN BUKU===
//        Start
//        Tampil halaman dashboard
//        Dipilih menu scan pengembalian buku
//        If permission check == true
//        Buka kamera
//        Jalankan fungsi readQR()
//        If qr tidak terbaca
//        Kembali ke baris 6
//        End if
//            Else
//        Meminta user menyetujui persyaratan
//                End if
//        End
//
//        //===DETAIL BUKU ADMIN===
//        Start
//        Tampil halaman detail buku asa
//        Sembunyikan button pinjam buku asa
//        Sembunyikan button love
//        If klik menu delete then
//            Dijalankan fungsi deleteBook()
//        If klik menu update then
//            Tampilkan halaman update buku asa
//            Ubah data yang diinginkan asa
//            If klik button simpan then
//                Dijalankan fungsi postBookData()
//            Else
//                Tetap di halaman update buku
//            End if asa
//        If klik button Ebook then asa
//            Tampilkan halaman ebook
//        Else
//            Tetap di halaman detail buku
//        End if
//        End
//
//
//
//        //===DETAIL BUKU MEMBER===
//        Start
//        Tampil halaman detail buku asa
//        Sembunyikan menu update
//        Sembunyikan menu delete
//        If klik button love then
//            Dijalankan fungsi favoriteViewModel()
//        If klik button pinjam buku then
//            Dijalankan fungsi doLoan()
//        If klik button Ebook then asa
//            Tampilkan halaman ebook
//        Else
//            Tetap di halaman detail buku
//        End
//
//
//
//        //===DETAIL USER ADMIN===
//        Start
//        Tampil halaman user profil asa
//        If klik menu delete then
//            Dijalankan fungsi deleteMember()
//        If klik menu update then
//            Tampilkan halaman update user asa
//            Ubah data yang diinginkan asa
//            If klik button simpan then
//                Dijalankan fungsi postUserData()
//            Else
//                Tetap di halaman update user
//            End if asa
//        If klik button ubah avatar
//            Pilih gambar asa
//            Tampilkan dialog konfirmasi
//            If klik button ya asa
//            Jalankan fungsi uploadImage()
//            Else
//                Batalkan perubahan()
//            End if
//        Else
//            Tetap di halaman user profil
//        End if asa
//        End
//
//
//
//        //===DETAIL USER MEMBER===
//        Start
//        Tampil halaman user profil asa
//        If klik button ubah avatar
//            Pilih gambar asa
//            Tampilkan dialog konfirmasi
//            If klik button ya asa
//                Jalankan fungsi uploadImage()
//            Else
//                Batalkan perubahan()
//            End if asa
//        Else
//        Tetap di halaman user profil
//        End if asa
//        End















//        Start
//        Tampil splash screen Tampil form login
//        Input username dan password
//            if login sukses then
//                Tampil halamaan dashboard aplikasi
//            Else
//                Tetap di halaman login
//            If pilih menu tambah buku then
//                Tampil halaman tambah buku
//                Tampil form field buku
//                Input judul buku
//                Input penulis buku
//                Input penerbit buku
//                Input tahun terbit buku
//                Input jumlah stok buku
//                Pilih kategori buku yang tersedia
//                Input deskripsi buku
//                Pilih gambar untuk poster buku
//                Pilih file pdf untuk ebook
//                If klik simpan then
//                    Upload data ke server
//                Else
//                    Kembali ke halaman dashaboard
//                End if
//            If pilih menu tambah admin then
//                Tampil halaman tambah user
//                Tampil form field user
//                Input username
//                Input password
//                Input nama depan
//                Input nama belakang
//                Input email
//                Input nomer handphone
//                Pilih jenis kelamin
//                Pilih jenjang
//                Input alamat
//                If klik simpan then
//                Upload data ke server
//                Else
//                Kembali ke halaman dashaboard
//                End if
//            If pilih menu daftar buku
//                Tampil halaman buku
//                Tampil daftar buku
//                If pilih salah satu buku then
//                    Tampil halaman detail buku
//                    If klik icon more then
//                        Tampil pilihan update dan delete
//                            If klik delete then
//                                Hapus buku dari daftar
//                            Else
//                                Kembali ke halaman detail buku
//                            End if
//                            If klik update then
//                                Tampil halaman update buku
//                                Tampil form field buku
//                                Ubah data yang diinginkan
//                                    If klik simpan then
//                                        Upload data terbaru ke server
//                                    Else
//                                        Kembali ke halaman detail buku
//                                    End if
//                            Else
//                                Kembali ke halaman detail buku
//                            End if
//                    Else
//                        Kembali ke halaman buku
//                    End if
//                Else
//                    Kembali ke halaman dashboard
//                End if
//            If pilih menu daftar user

    }
}