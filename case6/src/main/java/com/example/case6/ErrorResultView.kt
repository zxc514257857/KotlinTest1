package com.example.case6

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case6
 * @Author: zhr
 * @Date: 2022/1/20 11:05
 * @Version: V1.0
 */
class ErrorResultView(context: Context) : ResultView(context) {

    override fun getLayoutRes(): Int {
        return R.layout.result_view
    }

    @SuppressLint("SetTextI18n")
    override fun initData(resultView: View) {
        resultView.findViewById<TextView>(R.id.tv_result_status).text = "刷脸成功"
        resultView.findViewById<TextView>(R.id.tv_customer_info).text = "131****0562  **海"
    }
}