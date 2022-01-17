package com.example.case5

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case5
 * @Author: zhr
 * @Date: 2022/1/14 15:55
 * @Version: V1.0
 */
class MonitorService : Service() {

    private var isCycle = true
    private val netStatusBroadcast by lazy {
        NetStatusBroadcast()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTrimMemory(level: Int) {
        Log.e(
            "TAG",
            "${ErrorCodeEnum.SYSTEM_TRIM_MEMORY.description} + ${ErrorCodeEnum.SYSTEM_TRIM_MEMORY.code}"
        )
    }

    override fun onLowMemory() {
        Log.e(
            "TAG",
            "${ErrorCodeEnum.SYSTEM_LOW_MEMORY.description} + ${ErrorCodeEnum.SYSTEM_LOW_MEMORY.code}"
        )
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.e(
            "TAG",
            "${ErrorCodeEnum.SYSTEM_TASK_REMOVED.description} + ${ErrorCodeEnum.SYSTEM_TASK_REMOVED.code}"
        )
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("TAG", "doTask: ")
        doTask()
    }

    override fun onDestroy() {
        super.onDestroy()
        isCycle = false
        unregisterReceiver(netStatusBroadcast)
    }

    private fun doTask() {
        // 在协程中执行定时任务
        GlobalScope.launch(Dispatchers.IO) {
            try {
                doNet()
                while (isCycle) {
                    try {
                        doCycle()
                        delay(2 * 1000L)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun doNet() {
        // 广播没有像Activity和Service 有Utils类
        registerReceiver(netStatusBroadcast,
            IntentFilter().apply { addAction("android.net.conn.CONNECTIVITY_CHANGE") })
    }

    private fun doCycle() {
        Log.e("TAG", "doCycle: ")
    }
}