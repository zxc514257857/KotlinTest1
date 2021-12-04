package com.example.app.singtentest

import android.util.Log

/**
 * @Des: 饿汉式Kotlin写法
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/28 21:49
 * @Version: V1.0
 */
object TestKotlin2 {

    fun getInstance(): TestKotlin2 {
        return this
    }

    fun print() {
        Log.e("TAG", "TestKotlin2 print:::" + javaClass.hashCode())
    }
}