package com.zhr.case9.utils

import android.os.Environment
import java.io.File

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case9.utils
 * @Author: zhr
 * @Date: 2022/4/2 13:48
 * @Version: V1.0
 */
object FileUtils {

    private val MAIN_PATH = "whpe"

    private val SPLASH: String = try {
        System.getProperty("file.separator") ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        "/"
    }

    /**
     * 拼接文件完整路径
     * @param path 文件所在文件夹
     * @filename  文件名
     * @return
     */
    fun concatFilePath(path: String, filename: String): String {
        var dir = path
        var absolutePath = ""
        if (dir.endsWith(SPLASH)) absolutePath = dir + filename
        else absolutePath = dir + SPLASH + filename
        return absolutePath
    }

    fun concatFilePath(vararg path: String): String {
        var dir = path.first()
        path.toList().subList(1, path.size).iterator().forEach {
            if (it.endsWith(SPLASH)) dir += it
            else dir = dir + SPLASH + it
        }
        return dir
    }

    private fun getSDCardDownloadPath(): String {
        // sdcard - Download目录
        // /storage/emulated/0/Download
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
    }

    private fun getMainPath(): String {
        var path = MAIN_PATH
        try {
            makedir(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // sdcard - Download - whpe目录
        // /storage/emulated/0/Download/whpe
        path = concatFilePath(getSDCardDownloadPath(), path)
        return path
    }

    private fun makedir(path: String) {
        try {
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getZhrPath(): String {
        var path = "zhr"
        try {
            // sdcard - Download - whpe - zhr目录
            // /storage/emulated/0/Download/whpe/zhr
            path = concatFilePath(getMainPath(), path)
            makedir(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // 如果不加这个的话 不会有后面的斜杠
        // /storage/emulated/0/Download/whpe/zhr/
//        path = concatFilePath(path, "")
        return path
    }
}