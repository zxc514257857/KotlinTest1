package com.zhr.case9.dbmanager

import android.content.Context
import com.zhr.case9.gen.DaoMaster
import com.zhr.case9.gen.SignOutInfoDao
import com.zhr.case9.utils.MigrationHelper
import org.greenrobot.greendao.database.Database

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case9
 * @Author: zhr
 * @Date: 2022/4/2 11:23
 * @Version: V1.0
 */
class DbUpdateOpenHelper(context: Context, dbName: String) : DaoMaster.DevOpenHelper(context, dbName) {

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        // 使用父类的方法    删除所有的表（dropAllTables），然后重新创建（onCreate），不适用于正式上线环境
//        super.onUpgrade(db, oldVersion, newVersion)


//        val mUpdateTables = ArrayList<Class<out AbstractDao<*, *>?>?>()
//        mUpdateTables.add(SignOutInfoDao::class.java)
//        MigrationHelper.migrate(db, object : MigrationHelper.ReCreateAllTableListener {
//            override fun onCreateAllTables(db: Database?, ifNotExists: Boolean) {
//                DaoMaster.createAllTables(db, ifNotExists)
//            }
//
//            override fun onDropAllTables(db: Database?, ifExists: Boolean) {
//                DaoMaster.dropAllTables(db, ifExists)
//            }
//        }, *(mUpdateTables.toTypedArray()))


        when (newVersion) {
            0 -> {}
            1 -> {}
            2 ->  MigrationHelper.migrate(db, SignOutInfoDao::class.java) // 2版本中在SiteCacheInfo中新增siteState字段
            else -> {}
        }


//        if (newVersion == 2) {
//            // 在表中添加字段
//            db?.execSQL(StringBuffer().append(
//                    "ALTER TABLE",
//                    " site_cache_info",
//                    " ADD ",
//                    "SITE_STATE",
//                    " TEXT ",
//                    "DEFAULT NULL"
//                ).toString()
//            )
//            // 新增一个表 SiteCacheInfoDao.createTable(db, true)
//        }
    }
}