package com.example.case5

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

/**
 * 1、“”?.let{true} ?:fale  表示不为null返回true，为null返回false
 * 2、
 */
class MainActivity : AppCompatActivity() {

    lateinit var tv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        // ?. 和 ?: 以及return的连用
        Log.e("TAG", "return : ${test1()}")  // return : true

        // 协程使用    将执行分发到 UI线程
        GlobalScope.launch(Dispatchers.IO) {}
        // 将执行分发到主线程
        CoroutineScope(Dispatchers.Main).launch {}
        loadData()
        loadData1()
    }

    private fun initView() {
        tv = findViewById(R.id.tv)
    }

    private fun test1(): Boolean {
        var aaa: String? = ""   // aaa不为null  返回true
//        aaa = null  // aaa为null  返回false
        // 这段话的意思是，不为null 返回true 为null返回false
        val block = aaa?.let {
            true
        } ?: false
        return block
    }

    // 先创建一个范围，然后在范围中进行执行
    val uiScope1 = CoroutineScope(Dispatchers.Main)
    fun loadData() = uiScope1.launch {
        tv.text = "hahhahah"
        // 这两个是串行执行的
        val result1 = withContext(Dispatchers.IO) {
            // 协程中的睡眠
            delay(2000)
            // 报这样的错误：Only the original thread that created a view hierarchy can touch its views.
//            tv.text = "hahhahah1"
            Log.e("TAG", "loadData1: ${Thread.currentThread().name}")
            "result1"
        }
        val result2 = withContext(Dispatchers.IO) {
            delay(1000)
            // 报这样的错误：Only the original thread that created a view hierarchy can touch its views.
//            tv.text = "hahhahah2"
            Log.e("TAG", "loadData2: ${Thread.currentThread().name}")
            "result2"
        }
        Log.e("TAG", "result1:${result1},,,result2:${result2}")
    }

    fun loadData1() = GlobalScope.launch(Dispatchers.Main) {
        delay(2000)
//        Dispatchers.IO 会闪退    Dispatchers.Main 不会
//        tv.text = "hahhahah1"
    }

}