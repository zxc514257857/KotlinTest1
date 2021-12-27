package com.example.case3

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.blankj.utilcode.util.ThreadUtils
import com.example.case3.builder.CompanyClient
import com.example.case3.builder.Person
import com.example.case3.loger.IErrorCodeCallback
import com.example.case3.observable.EventBean
import com.example.case3.loger.LoggerUtils
import com.example.case3.loger.PropertiesPath
import com.example.case3.observable.BaseObservable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 888

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 通过自定义事件类型进行数据的发送和接收
        val baseObservable = BaseObservable<EventBean<Any?>>()
        baseObservable.addObserver { o, arg ->
            val eventBean = arg as EventBean<*>
            if (eventBean.code == 0x01) {
                Log.e("TAG", "baseObservable1: ${eventBean.data[0]}")
            } else if (eventBean.code == 0x02) {
                Log.e("TAG", "baseObservable2: ${eventBean.data[0] as Person.Builder}")
            }
        }
        baseObservable.updateData(
            EventBean(
                0x01,
                "test1"
            )
        )
        val builder = Person.Builder()
        builder.name("111").sex("nan").build()
        baseObservable.updateData(
            EventBean(
                0x02,
                builder
            )
        )

        // 检查目前是否有权限 ，如果没有权限则进入
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED
        ){
            Log.e("TAG1", "shouldShowRequestPermission: ${ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)}", )

//            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//                // 用户点击了拒绝返回true ； 没有申请过权限返回false ； 拒绝且不再提示返回false ； 权限申请过返回false
//
//            }else {
//                // 申请权限
//                ActivityCompat.requestPermissions(this@MainActivity,arrayOf(
//                        Manifest.permission.READ_EXTERNAL_STORAGE),
//                    REQUEST_CODE
//                )
//            }

            // 可以直接请求权限算了 不需要再进行详细的判断
            ActivityCompat.requestPermissions(this@MainActivity,arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
                REQUEST_CODE
            )
        } else {
            // 车载机不用请求权限，默认所有权限都请求到了
            Log.e("TAG1", "已经获取到了权限请求")
        }

        // 本地存日志的工具类
        LoggerUtils.getInstance().setDir(PropertiesPath.getInstance().getLogPath())
            .setFilePrefix("log")
            .setFileExt("log")
            .setSaveDays(2)
            .setLog2File(true)
            .setFileSplitSize(50 * 1024L)
            .setSaveLevel(LoggerUtils.INFO)
            .setErrorCodeCallback(object : IErrorCodeCallback {
                override fun onErrorCode(errorCodeStr: String) {}
            })

        ThreadUtils.executeByCachedAtFixRate(object : ThreadUtils.SimpleTask<Any?>() {
            override fun doInBackground(): Any? {
                LoggerUtils.getInstance().error(
                    "test1-${Thread.currentThread().name}",
                    "test:::${SimpleDateFormat("yyyyMMddHHmmss").format(Date())}"
                )
                return null
            }

            override fun onSuccess(p0: Any?) {}
        }, 2000, TimeUnit.MILLISECONDS)

        ThreadUtils.executeByCachedAtFixRate(object : ThreadUtils.SimpleTask<Any?>() {
            override fun doInBackground(): Any? {
                LoggerUtils.getInstance().error(
                    "test2-${Thread.currentThread().name}",
                    "test:::${SimpleDateFormat("yyyyMMddHHmmss").format(Date())}"
                )
                return null
            }

            override fun onSuccess(p0: Any?) {}
        }, 1000, TimeUnit.MILLISECONDS)

        // 建造者模式的写法  建造者模式是否可以在设置Bean类的时候使用呢？？？  我觉得可以
        CompanyClient.Builder().setCompanyName("杭州筑美").setCompanyAddr("杭州振宁路")
            .setCompanyEmployee("30").build()

        val zhr = Person()
        zhr.print("1", "2")
        val gaowei = Person.Builder().name("高伟").sex("男").build()
        Log.e("TAG", "gaowei:${gaowei}")
    }

    // 处理请求权限的结果回调
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 车载机每次都能获取到权限，这里回调成功
        if(requestCode == REQUEST_CODE){
            if (grantResults.size >0 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 5.回调权限申请成功
                Log.e("TAG1", "权限获取成功，请执行下面的逻辑")
            } else {
                // 6.回调权限申请拒绝
                Toast.makeText(this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

fun Person.print(q: String, w: String) {
    Log.e("TAG", "Person:::$q...$w")
}