package com.pactsafe.pactsafeandroidsdk.di

import com.pactsafe.pactsafeandroidsdk.BuildConfig
import com.pactsafe.pactsafeandroidsdk.PSApp
import com.pactsafe.pactsafeandroidsdk.data.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun createOkHttpClient(authenticate: Boolean = false): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()

    if (PSApp.debug) {
        client.addInterceptor(httpLoggingInterceptor)
    }

    if (authenticate) {
        client.addInterceptor(AuthenticationInterceptor())
    }
    return client.build()
}

inline fun <reified T> getRetrofitApi(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}

inline fun <reified T> getAppRetrofitApi(): T {
    return getRetrofitApi(
        createOkHttpClient(),
        BuildConfig.PS_BASE_URL
    )
}

class AuthenticationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authentication", "Bearer <api token>")
                .build()
        )
    }
}