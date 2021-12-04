package com.example.app.singtentest

import android.util.Log

/**
 * @Des: 线程安全懒汉式Kotlin写法
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/28 22:36
 * @Version: V1.0
 */
class TestKotlin3 private constructor(){
    // 私有构造方法

    companion object {
        // 声明这个对象
        private var mInstance: TestKotlin3? = null
            get() {
                if (null == field) {
                    field = TestKotlin3()
                }
                // 创建并返回这个对象
                return field
            }

        @Synchronized
        fun getInstance(): TestKotlin3 {
            return mInstance!!
        }
    }

    fun print() {
        Log.e("TAG", "TestKotlin3 print:::" + javaClass.hashCode())
    }
}