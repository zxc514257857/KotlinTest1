package com.zhr.case8

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case8
 * @Author: zhr
 * @Date: 2022/3/7 16:05
 * @Version: V1.0
 */
class StateView<T>(context: Context, owner: LifecycleOwner) : BaseView(context, owner) {

    // 在每个具体的View里面
    // liveData  放这个View里面
    // viewModel 放Activity里面


//    val stateViewLiveData by lazy {
//        savedStateHandle?.getLiveData<BaseEvent<T>>(
//            "${StateView::class.java}StateViewLiveData", null
//        )
//    }

    private var binding: ViewDataBinding? = null
    private var owner: LifecycleOwner? = null


    override fun getLayoutRes(): Int {
        return R.layout.activity
    }


//    fun registerObserver() {
//        owner?.let {
//            stateViewLiveData?.observe(it, {
//                binding.tv
//            })
//        }
//    }
}