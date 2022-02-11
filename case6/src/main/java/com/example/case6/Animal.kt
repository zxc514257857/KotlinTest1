package com.example.case6

import android.util.Log

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case6
 * @Author: zhr
 * @Date: 2022/2/10 9:19
 * @Version: V1.0
 */
class Animal(val name: String, val from: String) {

    fun getData() {
        Log.e("TAG", "name: $name,,from: $from")
    }
}