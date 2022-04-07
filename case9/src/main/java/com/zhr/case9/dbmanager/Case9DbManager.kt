package com.zhr.case9.dbmanager

import android.database.sqlite.SQLiteDatabase
import com.zhr.case9.base.AbstractDbManager
import com.zhr.case9.base.DBContext
import com.zhr.case9.gen.DaoMaster
import com.zhr.case9.utils.ContextUtils
import com.zhr.case9.utils.FileUtils
import org.greenrobot.greendao.database.DatabaseOpenHelper

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case9
 * @Author: zhr
 * @Date: 2022/4/2 13:39
 * @Version: V1.0
 */
object Case9DbManager : AbstractDbManager(
    ContextUtils.context, FileUtils.getZhrPath(), "case9.db"
) {

    override fun buildDbOpenHelper(dbContext: DBContext, dbName: String): DatabaseOpenHelper {
        return DbUpdateOpenHelper(dbContext, dbName)
    }

    override fun buildDaoMaster(sqLiteDatabase: SQLiteDatabase?): DaoMaster {
        return DaoMaster(sqLiteDatabase)
    }
}