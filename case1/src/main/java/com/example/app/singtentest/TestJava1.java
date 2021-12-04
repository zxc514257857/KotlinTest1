package com.example.app.singtentest;

import android.util.Log;

/**
 * @Des: 懒汉式Java写法
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/28 19:59
 * @Version: V1.0
 */
public class TestJava1 {

    // 私有构造方法
    private TestJava1() {
    }

    // 声明这个对象
    private static TestJava1 instance;

    // 创建这个对象并返回
    public static TestJava1 getInstance() {
        if(null == instance){
            instance = new TestJava1();
        }
        return instance;
    }

    public void print() {
        Log.e("TAG", "TestJava1 print:::" + getClass().hashCode());
    }
}

