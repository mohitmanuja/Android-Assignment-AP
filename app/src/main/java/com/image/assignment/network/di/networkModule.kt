package com.image.assignment.network.di

import com.image.assignment.network.RetrofitHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val networkModule = module {
    single { RetrofitHelper.provideHttpClient(androidApplication()) }
    single { RetrofitHelper.provideRetrofit(get()) }
}
