package com.example.app.singtentest

import android.util.Log

/**
 * @Des: 静态内部类Kotlin写法2
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/29 0:28
 * @Version: V1.0
 */
class TestKotlin6 private constructor() {
    // 私有构造方法

    class Holder {
        companion object {
            internal val getHolder: TestKotlin6 by lazy {
                TestKotlin6()
            }
        }
    }

    companion object {
        fun getInstance(): TestKotlin6 {
            return Holder.getHolder
        }
    }

    fun print() {
        Log.e("TAG", "TestKotlin6 print:::" + javaClass.hashCode())
    }
}