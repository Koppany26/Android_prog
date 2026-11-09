package com.example.progr3sshabitplannertracker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Ha emulátort használsz és a localhost-on (saját gépeden) fut a Docker:
    // Az Android emulátor a 10.0.2.2 IP címen éri el a gazdagép localhost-ját.
    // Ha fizikai telefont használsz, ide a géped valós belső IP címét kell írni (pl. http://192.168.1.5:3000)
    private const val BASE_URL = "http://10.0.2.2:3000/" // Cseréld le a backend portjára, ha nem 3000!

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    val apiService: ApiService by lazy {
        getClient().create(ApiService::class.java)
    }
}
