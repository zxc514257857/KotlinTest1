package com.example.app

import android.app.Activity
import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference


/**
 * @Des: 不会内存泄漏的Handler
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app
 * @Author: zhr
 * @Date: 2021/11/30 15:33
 * @Version: V1.0
 */
class WeakHandler(private val activity: Activity, var listen: (Message?) -> Unit) : Handler() {
    // 非静态内部类和匿名内部类会默认持有外部类的引用

    private val mWeakReference by lazy {
        WeakReference(activity)
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (null != mWeakReference.get()) {
            listen(msg)
        }
    }
}