package com.example.pdfnoter.notes

import android.content.Context
import android.net.ConnectivityManager

class Internet_check {
    fun isConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val netInfo = connectivityManager.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected && netInfo.isConnectedOrConnecting && netInfo.isAvailable) {
                return true
            }
        }
        return false
    }
}