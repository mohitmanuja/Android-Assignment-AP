package com.example.baseandroid.base

import android.app.Application

import com.example.baseandroid.base.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinApiExtension
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class BaseApplication : Application() {

    @KoinApiExtension
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    appModule
                )
            )
        }
    }

}
