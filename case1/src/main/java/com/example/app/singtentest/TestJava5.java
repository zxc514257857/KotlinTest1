package com.example.app.singtentest;

import android.util.Log;

/**
 * @Des: 静态内部类Java写法
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.singtentest
 * @Author: zhr
 * @Date: 2021/11/28 21:14
 * @Version: V1.0
 */
public class TestJava5 {

    // 私有构造方法
    private TestJava5() {
    }

    private static class Holder {
        private static TestJava5 getHolder = new TestJava5();
    }

    public static TestJava5 getInstance() {
        return Holder.getHolder;
    }

    public void print() {

        Log.e("TAG", "TestJava5 print:::" + getClass().hashCode());
    }
} 