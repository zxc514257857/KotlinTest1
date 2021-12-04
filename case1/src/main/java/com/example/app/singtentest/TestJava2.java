package com.example.app.singtentest;

import android.util.Log;

/**
 * @Des: 饿汉式Java写法
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/28 20:06
 * @Version: V1.0
 */
public class TestJava2 {

    // 私有构造方法
    private TestJava2() {
    }

    // 创建这个对象
    private static TestJava2 instance = new TestJava2();

    // 返回这个对象
    public static TestJava2 getInstance() {
        return instance;
    }

    public void print() {
        Log.e("TAG", "TestJava2 print:::" + getClass().hashCode());
    }
}