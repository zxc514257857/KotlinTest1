package com.example.app.observertest

import android.util.Log
import androidx.lifecycle.Observer

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.observertest
 * @Author: zhr
 * @Date: 2021/11/30 11:00
 * @Version: V1.0
 */
class LiveDataObserver : Observer<String> {

    override fun onChanged(t: String?) {
        Log.e("TAG", "onChanged1: $t")
    }
}