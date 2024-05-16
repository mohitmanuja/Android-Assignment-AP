package com.example.baseandroid.network.di

import com.example.baseandroid.network.RetrofitHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object NetworkModule {

    val networkModule = module {
        single { RetrofitHelper.provideHttpClient(androidApplication()) }
        single { RetrofitHelper.provideRetrofit(get()) }
    }
}