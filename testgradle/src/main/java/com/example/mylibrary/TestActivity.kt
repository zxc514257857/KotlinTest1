package com.example.mylibrary

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.mylibrary
 * @Author: zhr
 * @Date: 2021/12/8 11:16
 * @Version: V1.0
 */
class TestActivity : AppCompatActivity() {

    private val TAG = "TestActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppConstant.Cache.test++
        findViewById<TextView>(R.id.tv).text = "".plus(AppConstant.Cache.test)
        Log.e(TAG, "onCreate: ${AppConstant.Cache.test}")
    }
}