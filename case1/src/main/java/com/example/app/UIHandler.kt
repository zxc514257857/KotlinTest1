package com.example.app

import android.os.Handler
import android.os.Looper

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app
 * @Author: zhr
 * @Date: 2021/12/6 11:27
 * @Version: V1.0
 */
class UIHandler {

    companion object {
        private val handler = Handler(Looper.getMainLooper())

        fun post(runnable: Runnable) {
            handler.post(runnable)
        }

        fun postDelayed(runnable: Runnable, delayMillis: Long) {
            handler.postDelayed(runnable, delayMillis)
        }
    }
}