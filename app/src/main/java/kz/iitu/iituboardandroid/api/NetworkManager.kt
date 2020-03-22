package kz.iitu.iituboardandroid.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkManager(private var appContext: Context) {

    val isConnected: Boolean
        get() {
            val cm =
                appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return cm?.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getNetworkCapabilities(activeNetwork)?.run {
                        when {
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                            else -> false
                        }
                    } ?: false
                } else {
                    activeNetworkInfo?.run {
                        isConnected
                    } ?: false
                }
            } ?: false
        }
}