package pe.edu.upc.clinic.network

import com.androidnetworking.interceptors.HttpLoggingInterceptor

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient {
    companion object{

        val BASE_URL = "https://pethealthapi.herokuapp.com/api/"
        const val LOGIN = "login"
        const val APPOINTMENTS = "user/{user_id}/appts"
        private val MAX_TIME = 300
    }
    val webServices: PetHealthServices

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().readTimeout(MAX_TIME.toLong(), TimeUnit.SECONDS)
                .connectTimeout(MAX_TIME.toLong(), TimeUnit.SECONDS).addInterceptor(interceptor).build()

        val retrofit =
                Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                        .build()

        webServices = retrofit.create(PetHealthServices::class.java)
    }
}