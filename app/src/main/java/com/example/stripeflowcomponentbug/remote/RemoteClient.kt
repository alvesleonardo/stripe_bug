package com.example.stripeflowcomponentbug.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteClient @Inject constructor() {
    val logging = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    val httpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

    val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("https://doji-test.herokuapp.com/")
        .client(httpClient)
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    private companion object {
        private const val TIMEOUT_SECONDS = 15L
    }
}