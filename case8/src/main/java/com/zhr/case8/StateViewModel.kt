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
 * @Date: 2022/3/7 14:22
 * @Version: V1.0
 */
open class StateViewModel<T>(savedStateHandle: SavedStateHandle) : ViewModel() {

    val stateLiveData =
        savedStateHandle.getLiveData<BaseEvent<T>>(
            "${BaseViewModel::class.java}StateLiveData", null
        )
}