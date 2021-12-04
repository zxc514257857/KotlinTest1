package com.example.app.singtentest

import android.util.Log

/**
 * @Des: 静态内部类Kotlin写法
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/29 0:28
 * @Version: V1.0
 */
class TestKotlin5 private constructor() {
    // 私有构造方法

    private object Holder {
        val getHolder = TestKotlin5()
    }

    companion object {
        private val mInstance = Holder.getHolder

        @Synchronized
        fun getInstance(): TestKotlin5 {
            return mInstance
        }
    }

    fun print(){
        Log.e("TAG", "TestKotlin5 print:::" + javaClass.hashCode())
    }
}