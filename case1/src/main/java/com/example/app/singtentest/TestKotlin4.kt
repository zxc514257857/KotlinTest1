package com.example.app.singtentest

import android.util.Log

/**
 * @Des: 双重判断加锁懒汉式Kotlin写法
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/28 22:51
 * @Version: V1.0
 */
class TestKotlin4 private constructor() {
    // 私有构造方法

    companion object {
        // 第四种的写法是最推荐的，然后是方法五和方法三
        // 默认就是sync类型的，可以不用写，表示是线程同步的    none 表示不是线程同步的
        private val mInstance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            TestKotlin4()
        }

        @Synchronized
        fun getInstance(): TestKotlin4 {
            return mInstance
        }
    }

    fun print(){
        Log.e("TAG", "TestKotlin4 print:::" + javaClass.hashCode())
    }
}