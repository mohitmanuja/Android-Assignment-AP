package com.image.assignment.network


import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinApiExtension
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    private const val connectTimeout: Long = 40// 20s
    private const val readTimeout: Long = 40 // 20s
    val cacheSize = (5 * 1024 * 1024).toLong()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @KoinApiExtension
    fun provideHttpClient(context: Context): OkHttpClient {
        val myCache = Cache(context.cacheDir, cacheSize)

        return OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .cache(myCache)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .addInterceptor(NetworkUtils.getCommonHeaders())
            .addInterceptor(ChuckInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .build()

    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConfig.SERVER_BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                ).asLenient()

            )
            .client(okHttpClient)
            .build()
    }


}