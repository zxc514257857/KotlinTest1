package com.example.app.observertest

import android.util.Log
import java.util.*

/**
 * @Des: 测试观察者：Observer
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.observertest
 * @Author: zhr
 * @Date: 2021/11/30 10:14
 * @Version: V1.0
 */
class MyObserver : Observer {
    // 观察者类中进行被观察数据的接收

    override fun update(o: Observable?, arg: Any?) {
        Log.e("TAG", "update2: $arg")
    }
}