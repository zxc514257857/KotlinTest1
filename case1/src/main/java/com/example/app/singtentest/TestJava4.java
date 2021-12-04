package com.example.app.singtentest;

import android.util.Log;

/**
 * @Des: 双重判断加锁懒汉式Java写法
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/28 20:24
 * @Version: V1.0
 */
public class TestJava4 {

    // 私有构造方法
    private TestJava4() {
    }

    // 声明这个对象
    private volatile static TestJava4 instance;

    public static TestJava4 getInstance() {
        if (null == instance) {
            synchronized (TestJava4.class) {
                if (null == instance) {
                    instance = new TestJava4();
                }
            }
        }
        return instance;
    }

    public void print() {
        Log.e("TAG", "TestJava4 print:::" + getClass().hashCode());
    }
} 