package com.zhr.case9

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zhr.case9.dbmanager.SignOutInfoManager

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

        //
    }
}