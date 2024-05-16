package com.example.baseandroid.utils

import android.content.Context
import android.util.Log
import com.example.baseandroid.BuildConfig


private const val TAG = "IMG_ASSIGNMENT"

fun Context.logMsg(tag: String =">>>", msg: String) {
    if (BuildConfig.DEBUG) {
        Log.v(TAG, "--------------")
        Log.v("$TAG $tag", msg)
        Log.v(TAG, "--------------")
    }
}

fun Context.logErrorMsg(tag: String, msg: String) {
    if (BuildConfig.DEBUG) {
        Log.e(TAG, "--------------")
        Log.v("$TAG $tag", msg)
        Log.e(TAG, "--------------")
    }
}

fun Context.print(s: String) {
    if (BuildConfig.DEBUG) {
        println("$TAG : $s")
    }
}

