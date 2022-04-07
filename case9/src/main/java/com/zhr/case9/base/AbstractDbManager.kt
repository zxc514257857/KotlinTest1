package com.zhr.case9.base

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.zhr.case9.gen.DaoMaster
import com.zhr.case9.gen.DaoSession
import org.greenrobot.greendao.database.DatabaseOpenHelper
import org.greenrobot.greendao.query.QueryBuilder

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case9.base
 * @Author: zhr
 * @Date: 2022/4/2 11:36
 * @Version: V1.0
 */
abstract class AbstractDbManager(
    context: Context,
    dbBaseDirPath: String,
    dbName: String,
) {

    private var dbOpenHelper: DatabaseOpenHelper? = null
    private var daoMaster: DaoMaster? = null

    // daoSession要对外使用 ， 其他的看一下是否要对外
    var daoSession: DaoSession? = null

    init {
        val dbContext = DBContext(context, dbBaseDirPath).apply {
            this.getDatabasePath(dbName)
        }
        dbOpenHelper = buildDbOpenHelper(dbContext, dbName)
        daoMaster = buildDaoMaster(getDataBase(true))
        daoSession = daoMaster?.newSession()
    }

    abstract fun buildDbOpenHelper(dbContext: DBContext, dbName: String): DatabaseOpenHelper

    abstract fun buildDaoMaster(sqLiteDatabase: SQLiteDatabase?): DaoMaster

    private fun getDataBase(writeable: Boolean): SQLiteDatabase? {
        return if (writeable) {
            dbOpenHelper?.writableDatabase
        } else {
            dbOpenHelper?.readableDatabase
        }
    }

    fun setDebug(isDebug: Boolean) {
        QueryBuilder.LOG_SQL = isDebug
        QueryBuilder.LOG_VALUES = isDebug
    }

    fun closeDb() {
        closeHelper()
        closeDaoSession()
    }

    private fun closeHelper() {
        if (null != dbOpenHelper) {
            dbOpenHelper?.close()
            dbOpenHelper = null
        }
    }

    private fun closeDaoSession() {
        if (null != daoSession) {
            daoSession?.clear()
            daoSession = null
        }
    }
}