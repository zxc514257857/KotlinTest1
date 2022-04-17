package com.example.case5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast

class NetStatusBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            Log.e(this.javaClass.name, "网络发生改变")
            val connectivityManager: ConnectivityManager? =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val networkInfo = connectivityManager?.activeNetworkInfo
            if (networkInfo != null) {
                // ConnectivityManager: 0 移动网络 1 wifi网络 ....
                Log.e(this.javaClass.name,"网络类型：${networkInfo.type},网络是否可用:${networkInfo.isAvailable()}")
                // 这里为什么会报  java.lang.Exception: Toast callstack! 这个错，毫无原因
                Toast.makeText(context, "网络类型：${networkInfo.type},网络是否可用:${networkInfo.isAvailable()}", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(this.javaClass.name, "网络不可用")
                Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show()
            }
        }
    }
}