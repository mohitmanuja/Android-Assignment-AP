package com.image.assignment.network


import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit

@KoinApiExtension
class RetrofitServiceHelper : KoinComponent {
    val retrofit: Retrofit by inject()

    fun <S> createRetrofitService(serviceClass: Class<S>?): S {
        return retrofit.create(serviceClass)
    }

}
