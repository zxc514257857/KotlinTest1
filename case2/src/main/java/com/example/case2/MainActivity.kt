package com.example.case2

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils

/**
 * 1、application lib和android lib的不同
 * 2、静态变量的生命周期是整个应用程序
 * 3、gradle脚本的使用   https://www.cnblogs.com/dazhao/p/6692416.html
 * 4、android反编译工具的使用
 * 5、make project和rebuild project的区别
 */
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
        // 在module的build.gradle文件中配置了编译时替换configChanges逻辑 见testgradle module中的build.gradle配置
        // 通过gradle脚本修改Manifest文件中的内容，修改完成然后编译一下，在build-intermediates-packaged_manifests查看Manifest文件是否修改成功
        // gradle脚本通过一系列task进行自动化脚本的构建
        // gradle脚本一般通过groovy或者是kotlin DSL进行代码的编写

        // gradlew assembleWUHANDebug   gradlew assembleHANGZHOUDebug
        // 从BuildConfig中取gradle.properties文件的值
        Log.e(TAG, "cityName: ${BuildConfig.cityName}")
        Log.e(TAG, "isApp: ${BuildConfig.isApp}")
        Log.e(TAG, "url: ${BuildConfig.url}")
        // 获取resValue 里面配置的值
        Log.e(TAG, "resValue: ${resources.getString(R.string.welcome)}")
        // BuildConfigField 可以放置在  defaultConfig目录下，也可以放置在 buildTypes - release/debug 目录下

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
        // Rebuild Project 和Clean Project的功能差不多，都是先删除之前的编译文件再生成新编译的文件

        // android 嵌入式学习
        // 嵌入式平台：wince（微软）、linux、android、QNX（黑莓 quick unix 嵌入式实时操作系统）、AliOS、IOS、特斯拉车载操作系统
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG", "onDestroy: ")
    }
}