package com.zhr.case8

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LifecycleOwner

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case8
 * @Author: zhr
 * @Date: 2022/3/7 17:56
 * @Version: V1.0
 */
abstract class BaseView {

    // 初始化View在Base里面

    private var rootView: View? = null

    constructor(context: Context, owner: LifecycleOwner) {
        rootView = LayoutInflater.from(context).inflate(getLayoutRes(), null)
    }

    abstract fun getLayoutRes(): Int
}