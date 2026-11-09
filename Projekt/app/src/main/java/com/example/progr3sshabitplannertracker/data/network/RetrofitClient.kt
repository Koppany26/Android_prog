package com.example.progr3sshabitplannertracker.data.network

import android.content.Context
import com.example.progr3sshabitplannertracker.utils.LocalDateTimeAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Docker localhost / adb reverse
    private const val BASE_URL = "http://localhost:8080/"

    @Volatile
    var appContext: Context? = null
        private set

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    @Volatile
    private var instance: ApiService? = null

    fun getInstance(context: Context? = null): ApiService {
        val ctx = context ?: appContext
        return instance ?: synchronized(this) {
            instance ?: buildRetrofitClient(ctx).also { instance = it }
        }
    }

    private fun buildRetrofitClient(context: Context?): ApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val builder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(logging)

        if (context != null) {
            builder.addInterceptor(AuthInterceptor(context))
        }

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(builder.build())
            .build()
            .create(ApiService::class.java)
    }
}
