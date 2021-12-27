package com.example.case2

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var isClick: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // android 常用的lib有两种： application lib和android lib（application lib之间是平等的关系 每个lib可以单独使用）
        // （android lib之间是一主多副的关系 只有主lib可以单独使用）

        // manifest中 main、launcher设置的是桌面图标  main、home、default 设置的是替换启动器

        // Application 中可以存整个应用程序的临时缓存数据，或者可以存在静态变量中(静态变量不管在单独文件中或者activity中，其生命周期都是整个应用程序)
        AppConstant.Cache.test++
        val textView = findViewById<TextView>(R.id.tv)
        textView.text = "".plus(AppConstant.Cache.test)
        Log.e(TAG, "onCreate: ${AppConstant.Cache.test}")

        val ivFaceStatus = findViewById<ImageView>(R.id.iv_face_status)
        ivFaceStatus.setOnClickListener {
            if (!isClick) {
                (ivFaceStatus.background as GradientDrawable).setColor(Color.RED)
            } else {
                (ivFaceStatus.background as GradientDrawable).setColor(Color.GREEN)
            }
            isClick = !isClick
        }

        textView.setOnClickListener {
            ActivityUtils.startActivity(packageName, "com.example.mylibrary.TestActivity")
//            startActivity(Intent(this, TestActivity::class.java))
        }

        // 解决activity多次进入，不要为了解决而解决，而要找到其onCreate 多次调用的根本原因
        // 可以考虑下 AndroidManifest中的属性设置：launchMode、screenOrientation、configChanges

        // 所有的launchMode 设置：
        // android:configChanges="keyboardHidden|screenSize|orientation|locale|touchscreen|fontScale|mcc|mnc|smallestScreenSize|density|layoutDirection|screenLayout|colorMode|keyboard|navigation|uiMode"

        // 使用gradle脚本动态替换Manifest文件中的内容
        // gradle是编译时生效  java代码是运行时生效
        // 在module的build.gradle文件中配置了编译时替换configChanges逻辑 ：：：见本build.gradle配置

        // android 反编译
        // 需要使用这三个软件： apktool、dex2jar和JD-gui
        // apktool 将apk反编译为smali 项目 ，同时将apk反编译为dex 项目
        // 通过dex2jar 将dex文件转化为jar
        // 通过JD-gui导入jar 查看java源码
        // 通过AXMLPrinter2 查看AndroidManifest文件
        // 用来验证 本module下的activity以及其他module下的activity是否可以通过配置gradle来动态替换configChanges属性
        // 事实证明，只要依赖到主module之后，其实所有的配置都可以配置到主module就可以了，子module中的内容编译之后都会统一在主module中

        // Build栏目下 Make Project 和Rebuild Project的区别
        // Make Project 是增量编译，不Clean，比Rebuild Project快
        // Rebuild Project 是先Clean再编译，速度比较慢

//        BuildConfig.TEST_CONFIG

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG", "onDestroy: ")
    }
}