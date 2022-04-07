package com.zhr.case9.base

import android.content.Context
import android.content.ContextWrapper
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.File
import java.lang.StringBuilder

/**
 * @Des:
 *
 *
 *
 * @Title: DBContext
 * @Project: NetModule
 * @Package: com.whpe.pos.libbasedb
 * @Author: symb
 * @Date: 2022年02月10日 15:24
 * @Version:V1.0
 */
class DBContext(base: Context, private val dbBaseDirPath: String) : ContextWrapper(base) {

    override fun getDatabasePath(name: String?): File {
        if (dbBaseDirPath.isNullOrEmpty()) {
            val dbpath = super.getDatabasePath(name)
            return dbpath
        }
        // getDatabasePath: /storage/emulated/0/Download/whpe/zhr
        Log.e("TAG", "getDatabasePath: $dbBaseDirPath")
        val dbBaseDirFile = File(dbBaseDirPath)
        if (!dbBaseDirFile.exists()) {
            val mkdirs = dbBaseDirFile.mkdirs()
            Log.e("TAG", "mkdirs: $mkdirs")
        }

        val dbPath = StringBuilder().apply {
            append(dbBaseDirFile.path)
            append(File.separator)
            append(baseContext.packageName)
        }.also {
            val dbDirFile = File(it.toString())
            if (!dbDirFile.exists()) {
                val mkdirs = dbDirFile.mkdirs()
            }
        }.apply {
            append(File.separator)
            append(name)
        }.toString()
        Log.e("TAG", "dbPath: $dbPath")
        val dbFile = File(dbPath)
        if (!dbFile.exists()) {
            val path = try {
                val createNewFileResult = dbFile.createNewFile()
                if (createNewFileResult) {
                    dbFile
                } else {
                    super.getDatabasePath(name)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                super.getDatabasePath(name)
            }
            return path
        }
        return dbFile
    }

    override fun openOrCreateDatabase(
        name: String?,
        mode: Int,
        factory: SQLiteDatabase.CursorFactory?,
    ): SQLiteDatabase {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory)
    }

    override fun openOrCreateDatabase(
        name: String?,
        mode: Int,
        factory: SQLiteDatabase.CursorFactory?,
        errorHandler: DatabaseErrorHandler?,
    ): SQLiteDatabase {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory)
    }
}