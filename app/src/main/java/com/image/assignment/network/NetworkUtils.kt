package com.image.assignment.network


import com.image.assignment.BuildConfig
import okhttp3.Interceptor
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent


@KoinApiExtension
object NetworkUtils : KoinComponent {

    fun getCommonHeaders(): Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.header("Content-Type", "application/json")
            builder.header("app-version", BuildConfig.VERSION_NAME)
            builder.header("app-version-code", BuildConfig.VERSION_CODE.toString())
            builder.header("os", "android")

            return@Interceptor chain.proceed(builder.build())
        }
    }


}
