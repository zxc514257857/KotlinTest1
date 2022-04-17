package com.zhr.test.androidutilscodetest

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RomUtils
import java.util.concurrent.atomic.AtomicInteger

/**
 * AppUtils.getAppsInfo() 获取应用程序包名，versioncode，versionname等数据
 * ClipboardUtils 可以设置剪贴板监听，实时获取剪贴板数据
 * RomUtils 可以获取主板参数信息
 * DangerousUtils.installAppSilent()  通过pm install的方式静默安装应用程序
 * AppUtils.installApp()  手动安装应用程序（支持到android 8.0）
 * PinyinUtils    中文转英文 中文转英文首字母等
 *
 *
 * 1、kotlin中 until和.. 的区别：until不包含右边数据 ，..包含右边数据，左边的数据都是包含的
 * 2、AtomicInteger.incrementAndGet() 可以进行线程安全的累加操作
 * 3、handler.post() 用于切换线程     handler.sendEmptyMsg(0) 也是用于切换线程
 * 4、Thread{xxx}.start()  开启一个子线程进行操作
 */
class MainActivity : AppCompatActivity() {

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 自己实现获取应用程序数据
        val installedPackages = packageManager.getInstalledPackages(0)
        for (packageInfo in installedPackages) {
            val packageName = packageInfo.packageName
            val versionName = packageInfo.versionName
            val versionCode = packageInfo.versionCode
            val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            LogUtils.e("packageName:$packageName, versionName:$versionName, versionCode:$versionCode, appName:$appName")
        }

        // 用工具类实现获取应用程序参数数据
        val appsInfo = AppUtils.getAppsInfo()
        val atomicInteger = AtomicInteger()
        for (app in appsInfo) {
            atomicInteger.incrementAndGet()
            LogUtils.e("app:$app, atomicInteger:${atomicInteger.get()}")
        }

//        // 隔一段时间获取剪贴板数据
//        val handler = object : Handler() {
//            @SuppressLint("HandlerLeak")
//            override fun handleMessage(msg: Message) {
//                super.handleMessage(msg)
//                if (msg.what == 0) {
//                    val text = ClipboardUtils.getText()
//                    LogUtils.e("ClipboardText: $text")
//                }
//            }
//        }
//
//        ThreadUtils.executeBySingleAtFixRate(object : ThreadUtils.SimpleTask<Unit>() {
//            override fun doInBackground() {
//                handler.sendEmptyMessage(0)
//            }
//
//            override fun onSuccess(result: Unit?) {}
//        }, 1, TimeUnit.SECONDS)

        // 设置剪贴板监听器
        ClipboardUtils.addChangedListener {
            val text = ClipboardUtils.getText()
            LogUtils.e("ClipboardText: $text")
        }

        // 获取Rom信息
        LogUtils.e("getRomInfo: ${RomUtils.getRomInfo()}")

        // 获取当前所在国家
        val countryByLanguage = CountryUtils.getCountryByLanguage()
        val countryBySim = CountryUtils.getCountryBySim()
        LogUtils.e("countryByLanguage:$countryByLanguage,countryBySim:$countryBySim")

        // 在子线程进行耗时性操作
        Thread {
            LogUtils.e("installAppSilent: start")
            // 静默安装工具类
            val installAppSilent =
                DangerousUtils.installAppSilent("sdcard/com.netease.cloudmusic_8.5.10_8005010.apk")
            LogUtils.e("installAppSilent:$installAppSilent")
            // 如果要更新UI，需要通过线程切换切换到主线程，如果不更新UI则不需要切换到主线程
        }.start()

        // 这里的定位感觉不太好用（必须要有GPS等硬件参数支持），没有百度地图定位好用
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val gpsEnabled = LocationUtils.isGpsEnabled()
        LogUtils.e("gpsEnabled:$gpsEnabled")
        LocationUtils.register(1000, 1, object : LocationUtils.OnLocationChangeListener {
            override fun getLastKnownLocation(location: Location?) {
            }

            override fun onLocationChanged(location: Location?) {
                val latitude = location?.latitude
                val longitude = location?.longitude
                val address = LocationUtils.getAddress(latitude!!, longitude!!)
                LogUtils.e("address:$address")
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }
        })

        val pinyin1 = PinyinUtils.ccs2Pinyin("我是中国人")
        val pinyin2 = PinyinUtils.ccs2Pinyin("woshizhongguoren")
        LogUtils.e("pinyin1:$pinyin1, pinyin2:$pinyin2")
        val pinyin3 = PinyinUtils.getSurnamePinyin("单雄信")
        val pinyin4 = PinyinUtils.getSurnameFirstLetter("单雄信")
        LogUtils.e("pinyin3:$pinyin3, pinyin4:$pinyin4")
        val pinyin5 =
            PinyinUtils.getSurnamePinyin("单雄信") + PinyinUtils.ccs2Pinyin("单雄信".substring(1,
                "单雄信".length))
        val pinyin6 =
            PinyinUtils.getSurnameFirstLetter("单雄信") + PinyinUtils.getPinyinFirstLetters("单雄信".substring(
                1,
                "单雄信".length))
        LogUtils.e("pinyin5:$pinyin5, pinyin6:$pinyin6")
    }
}