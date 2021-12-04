package com.example.app.singtentest

import android.util.Log

/**
 * @Des: 懒汉式Kotlin写法
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/28 21:24
 * @Version: V1.0
 */
class TestKotlin1 private constructor() {
    // 私有构造方法

    companion object {
        // 声明这个对象
        private var mInstance: TestKotlin1? = null
            get() {
                if (null == field) {
                    field = TestKotlin1()
                }
                // 创建这个对象并返回
                return field
            }

        fun getInstance(): TestKotlin1 {
            return mInstance!!
        }
    }

    fun print(){
        Log.e("TAG", "TestKotlin1 print:::" + javaClass.hashCode())
    }
}