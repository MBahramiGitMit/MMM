package ir.mehdibahrami.mbmovie.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkChecker @Inject constructor(@ApplicationContext private val context: Context) {
    fun isNetConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (cm.activeNetwork != null) {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            } else {
                cm.activeNetworkInfo?.isConnected == true
            }
        } else {
            false
        }
    }
}

