package com.example.app.singtentest

import android.content.Context
import android.util.Log

/**
 * @Des: 单例模式传参
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/30 10:01
 * @Version: V1.0
 */
class TestKotlin7 private constructor() {
    // 私有构造方法

    private var context: Context? = null

    companion object {
        private val mInstance by lazy {
            TestKotlin7()
        }

        @Synchronized
        fun getInstance(): TestKotlin7 {
            return mInstance
        }
    }

    fun print(context : Context){
        this.context = context
        Log.e("TAG", "TestKotlin7 print:::" + javaClass.hashCode())
    }
}