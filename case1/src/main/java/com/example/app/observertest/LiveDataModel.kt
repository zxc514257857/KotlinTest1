package com.example.app.observertest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.observertest
 * @Author: zhr
 * @Date: 2021/11/30 11:02
 * @Version: V1.0
 */
class LiveDataModel : ViewModel() {

    val observeData by lazy {
        MutableLiveData<String>()
    }
}