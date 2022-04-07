package com.zhr.case8

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case8
 * @Author: zhr
 * @Date: 2022/3/7 9:41
 * @Version: V1.0
 */
open class BaseViewModel<T>(savedStateHandle: SavedStateHandle) : ViewModel() {

    val baseLiveData =
        savedStateHandle.getLiveData<BaseEvent<T>>(
            "${BaseViewModel::class.java}BaseLiveData", null
        )
}