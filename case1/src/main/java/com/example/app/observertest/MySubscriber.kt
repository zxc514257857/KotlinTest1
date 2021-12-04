package com.example.app.observertest

import android.util.Log
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.observertest
 * @Author: zhr
 * @Date: 2021/11/30 14:53
 * @Version: V1.0
 */
class MySubscriber {

    // 这里的线程类型有五种 MAIN 表示接收的线程在主线程执行
    // POSTING 表示接收的线程和发送的线程在同一个线程
    // MAIN_ORDER 表示接收的线程在主线程并且以同步的方式执行接收
    // ASYNC 表示不管接收的在哪个线程 都会新创建一个线程执行
    // BACKGROUND 表示如果接收的在主线程则会创建一个线程去执行，如果在子线程这会使用这个子线程去执行

    // 如果设置了黏性事件，这里的sticky 要设置为true
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun myMainSubscriber(eventBean: EventBean<String>) {
        Log.e("TAG", "mySubscriber1:")
        if (eventBean.code == AppConstant.EventCode.MAIN_POST) {
            Log.e("TAG", "mySubscriber1: ${eventBean.data[0]}")
        }
    }

    // 发送后，这两个方法都会接收到数据  根据code去判断在哪里进行处理
    @Subscribe(threadMode = ThreadMode.ASYNC, sticky = true)
    fun myAsyncSubscriber(eventBean: EventBean<String>) {
        Log.e("TAG", "mySubscriber2:")
        if (eventBean.code == AppConstant.EventCode.ASYNC_POST) {
            Log.e("TAG", "mySubscriber2: ${eventBean.data[0]}")
        }
    }
}