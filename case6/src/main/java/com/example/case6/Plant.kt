package com.example.case6

import android.util.Log

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case6
 * @Author: zhr
 * @Date: 2022/2/10 9:23
 * @Version: V1.0
 */
class Plant {

    private var name: String? = null
    private var from: String? = null

    constructor(name: String, from: String) {
        this.name = name
        this.from = from
    }

    fun getData(){
        Log.e("TAG", "name: $name,,from: $from")
    }
}