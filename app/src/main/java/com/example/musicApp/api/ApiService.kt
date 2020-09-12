package com.example.musicApp.api

import com.example.musicApp.data.SearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET("search")
    fun searchAlbum(@Query("term") term: String): Call<SearchResponse>

    companion object {
        private const val BASE_URL = " https://itunes.apple.com/"

        fun create(): ApiService {
            val mInterceptor = HttpLoggingInterceptor()
            val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.MINUTES).readTimeout(15, TimeUnit.MINUTES)
                .writeTimeout(15, TimeUnit.MINUTES).addInterceptor(mInterceptor).build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
