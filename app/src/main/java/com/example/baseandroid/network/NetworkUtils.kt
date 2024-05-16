package com.example.baseandroid.network


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import com.example.baseandroid.BuildConfig
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

fun Context.isConnectedToNetwork(): Boolean {
    val conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//    val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
//    return isConnected

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network: Network? = conMgr.getActiveNetwork()
        val networkCapabilities: NetworkCapabilities? = conMgr.getNetworkCapabilities(network)
        networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    } else {
        // below API Level 23
        (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo()
            ?.isAvailable() ?: false
                && conMgr.getActiveNetworkInfo()?.isConnected() ?: false)
    }
}