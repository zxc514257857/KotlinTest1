package com.example.case6

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case6
 * @Author: zhr
 * @Date: 2022/1/19 10:31
 * @Version: V1.0
 */
abstract class ResultView : ConstraintLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val rootView = LayoutInflater.from(context).inflate(R.layout.result_view, this)
        initData(rootView)
    }

    abstract fun getLayoutRes(): Int
    abstract fun initData(resultView: View)
}