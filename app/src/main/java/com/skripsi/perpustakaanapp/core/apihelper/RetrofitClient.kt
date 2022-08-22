package com.skripsi.perpustakaanapp.core.apihelper

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //initialization api address
//        private const val BASE_URL: String = "http://192.168.43.253:9080/api/library/"
    private const val BASE_URL: String = "http://185.201.9.17:9080/api/library/"
    fun create(): Api {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
        return retrofit.create(Api::class.java)
    }
}