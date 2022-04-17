package com.zhr.case9

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zhr.case9.dbmanager.SignOutInfoManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv).setOnClickListener {
            Log.e("TAG", "onCreate: ${SignOutInfoManager.saveSignOutInfo("111", true)}")
        }

        // ftl文件是什么？ 在greendao源码中找到的 : FreeMarker Template Language

        // GreenDao 字段最好要用对象类型，不要用拆箱之后的类型，因为数据库中存的本来就是对象，如果用拆箱之后的类型会闪退

        // 查看类的继承关系 ctrl + h

        // Retrofit 封装
        ServiceGenerator.createService(ApiService::class.java)
            .login("xxxxx", "123456").enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        if(response.isSuccessful){
                            val jsonString = response.body()?.string()
                            Log.e("TAG", "onResponse1: $jsonString")
                        }else {
                            Log.e("TAG", "onResponse2")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("TAG", "onResponse4")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("TAG", "onResponse3")
                }
            })

        // AndroidStudio 4.0新增的功能：动作编辑器（Motion Editor）、布局检查器（Layout Inspector）升级、布局验证（Layout Validation）
        // CPU 分析器（CPU Profiler）升级、构建分析（Build Analyzer）
        // 动作编辑器：MotionLayout的创建和预览更加容易
        // 布局检查器：App UI调试更加容易
        // 布局验证：多设备适配更加方便
        // CPU分析器：便于对CPU中的线程活动进行更精细的分析
        // 构建分析：分析构建打包的应用时长，并对其中的问题提出解决方案
        // AndroidStudio4.0 IDE , 里面的文件I、E、M、F、V、C等标识的意思
        // AndroidStudio 4.0 TextClock 控件 在预览图中无法预览， 报这个错误Exception raised during rendering ，代码可以使用但会使预览失败 ， 建议写好之后将那部分注释掉
    }
}