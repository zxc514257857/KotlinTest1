package com.example.app

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * @Des:
 *
 * @Title:
 * @Project: FourInOne_ZhenJiang
 * @Package: com.whpe.pos.uibeijingroadassociation.view.activity.widget
 * @Author: zhr
 * @Date: 2021/12/1 10:07
 * @Version: V1.0
 */
class SignView : ConstraintLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
    ) {
        val signView = LayoutInflater.from(context).inflate(R.layout.ui_beijing_sign_in, this, true)
//        val signView = View.inflate(context, R.layout.ui_beijing_sign_in, this)


    }
}