package com.example.app.observertest

import java.util.*

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.observertest
 * @Author: zhr
 * @Date: 2021/11/30 10:15
 * @Version: V1.0
 */
class MyObservable private constructor(): Observable() {
    // 被观察者类中进行观察者的注册和数据的发送

    companion object{
        private val mInstance by lazy {
            MyObservable()
        }
        @Synchronized
        fun getInstance(): MyObservable{
            return mInstance
        }
    }

    // 对Observable进行了一层封装，添加了post数据的方法（notifyObservers() 这个方法需要setChanged才能发送数据）
    fun post(obj: Any?) {
        setChanged()
        notifyObservers(obj)
    }
}