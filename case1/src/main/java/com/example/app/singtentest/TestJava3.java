package com.example.app.singtentest;

import android.util.Log;

/**
 * @Des: 线程安全懒汉式Java写法
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/28 20:18
 * @Version: V1.0
 */
public class TestJava3 {

    // 私有构造方法
    private TestJava3() {
    }

    // 声明这个对象
    private static TestJava3 instance;

    // 创建对象并返回 通过使用sync关键字进行线程的同步 实现线程的安全
    public synchronized static TestJava3 getInstance() {
        if (null == instance) {
            instance = new TestJava3();
        }
        return instance;
    }

    public void print() {
        Log.e("TAG", "TestJava3 print:::" + getClass().hashCode());
    }
} 