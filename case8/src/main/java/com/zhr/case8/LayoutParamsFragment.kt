package com.zhr.case8

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case8
 * @Author: zhr
 * @Date: 2022/3/16 11:33
 * @Version: V1.0
 */
class LayoutParamsFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 先创建外层布局
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        linearLayout.setBackgroundResource(R.color.black)
        val textView = TextView(context)
        textView.layoutParams = RelativeLayout.LayoutParams(
            320, 320
        )
        textView.text = "Hello World!"
        textView.setBackgroundResource(R.color.purple_200)
        // 告诉父布局，如何摆放自己 addView(view, params)
        linearLayout.addView(textView)
        linearLayout.gravity = Gravity.CENTER
        return linearLayout
    }
}