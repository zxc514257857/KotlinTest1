package com.example.case3.loger

import android.util.Log
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LoggerUtils private constructor(){

    private var mIErrorCodeCallback : IErrorCodeCallback?= null

    companion object{
        const val VERBOSE = 2
        const val DEBUG = 3
        const val INFO = 4
        const val WARN = 5
        const val ERROR = 6
        const val ASSERT = 7

        private val mInstance : LoggerUtils by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            LoggerUtils()
        }

        fun getInstance() = mInstance
    }

    fun setDir(dirPath: String) : LoggerUtils {
        LogUtils.getConfig().dir = dirPath
        return this
    }

    fun setSaveLevel(@LogType level: Int) : LoggerUtils {
        LogUtils.getConfig().setFileFilter(level)
        return this
    }

    fun setFilePrefix(prefix: String) : LoggerUtils {
        LogUtils.getConfig().filePrefix = prefix
        return this
    }

    fun setSaveDays(days: Int) : LoggerUtils {
        LogUtils.getConfig().saveDays = days
        return this
    }

    fun setLog2File(save2File: Boolean) : LoggerUtils {
        LogUtils.getConfig().isLog2FileSwitch = save2File
        return this
    }

    fun setFileExt(extension: String) : LoggerUtils {
        LogUtils.getConfig().isLogHeadSwitch = false
        /** globalTag 不要启用，存在溢出*/
        LogUtils.getConfig().globalTag = ""
        LogUtils.getConfig().fileExtension = extension
        return this
    }

    fun setFileSplitSize(sizeInByte: Long) : LoggerUtils {
        return setDateFileWriter(sizeInByte)
//        return setFileNumFileWriter(sizeInByte)
    }

    private fun addHead(content: String): String{
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd ", Locale.getDefault())
        val headStr = simpleDateFormat.format(Date())
        return "$headStr$content"
    }
    private fun setFileNumFileWriter(sizeInByte: Long): LoggerUtils {
        LogUtils.getConfig().setFileWriter { file, content ->

            try {
                writeNewLogByFileNum(file, addHead(content), sizeInByte)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(this.javaClass.name, "保存日志失败 error = ${e.message}")
            }
        }
        return this
    }


    private fun setDateFileWriter(sizeInByte: Long): LoggerUtils {
        Log.e("TAG", "exist1: ${File("/storage/emulated/0/Download/whpe/log/log_2021_12_16_com.example.case3.log").exists()}")
        // 删除文件后，在这个方法中会重建文件
        LogUtils.getConfig().setFileWriter { file, content ->
            try {
//                Log.e("TAG", "exist2: ${File(file).exists()}")
//                Log.e("TAG", "setDateFileWriter: ${File(file).name}")
//                Log.e("TAG", "file111: $file")
                writeNewLog(file, content, sizeInByte)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(this.javaClass.name, "保存日志失败 error = ${e.message}")
            }
        }
        return this
    }

    fun setErrorCodeCallback(callback : IErrorCodeCallback) : LoggerUtils {
        this.mIErrorCodeCallback = callback
        return this
    }

    private fun writeNewLog(filePath : String, content : String, sizeInByte: Long){
        try {
            val oldFile = File(filePath)
            // 这里永远会返回 true
            if(oldFile.exists()) {
                val createNewFile = oldFile.createNewFile()
                // createNewFile 这里会出现创建失败，返回false
//                Log.e("TAG", "writeNewLog: 111,,,createNewFile:${createNewFile}")
            }else {
//                Log.e("TAG", "writeNewLog: 222")
            }

            if(oldFile.length() > sizeInByte){
                val dirFile = oldFile.parentFile
                if(false == dirFile?.exists()) dirFile.mkdirs()
                val logFiles = dirFile.listFiles()
                val cnt = logFiles.size
                val fileExt = LogUtils.getConfig().fileExtension
                val backUpName = "${oldFile.name.removeSuffix(fileExt)}_${cnt}${fileExt}"
                val tempPath = PathUtil.concatFilePath(dirFile.absolutePath, backUpName)
                val tempFile = File(tempPath)
                if(!tempFile.exists()) tempFile.createNewFile()

                if(FileUtils.copy(oldFile, tempFile)){
                    oldFile.delete()
                    oldFile.createNewFile()
                }
            }
            val writeFileFromString = FileIOUtils.writeFileFromString(oldFile, content, true)
            // 向log文件夹中追加字符串 成功  返回true
//            Log.e("TAG", "writeFileFromString:::${writeFileFromString}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(this.javaClass.name, "保存日志失败 error ${e.message}")
        }
    }

    private fun writeNewLogByFileNum(filePath : String, content : String, sizeInByte: Long){
        try {
            val file = File(filePath)

            val dirFile = file.parentFile
            val fileExt = LogUtils.getConfig().fileExtension

            var writeFile: File

            val logFileList = ArrayList<File>()
            var logFileListbefore = dirFile.listFiles()
            logFileListbefore!!.let {
                val array = it.filter {
                    try {
                        it?.name?.replace(fileExt, "")?.toLong() ?: false
                        true
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false
                    }
                }

                logFileList.addAll(array)
            }
            logFileList.sortByDescending { it.name.replace(fileExt,"").toLong()}

            writeFile = if(!logFileList.isNullOrEmpty()){
                logFileList.first()
            }else{
                val oldFile = File(PathUtil.concatFilePath(dirFile.absolutePath, "0${fileExt}"))
                if(!oldFile.exists()) oldFile.createNewFile()
                Log.d(this.javaClass.name, "log11 old name[${oldFile.absolutePath}]")
                oldFile
            }

            Log.d(this.javaClass.name, "log11 old name[${writeFile.absolutePath}][${writeFile.length()}]")
            if(writeFile.length() > sizeInByte){
                if(false == dirFile?.exists()) dirFile.mkdirs()
                val cnt = logFileList.first().name.replace(fileExt,"").toLong()+1
                val newFilePath = PathUtil.concatFilePath(dirFile.absolutePath, "${cnt}${fileExt}")
                val newFile = File(newFilePath)
                if(!newFile.exists()) newFile.createNewFile()
                writeFile = newFile
            }
            Log.d(this.javaClass.name, "log11 write name[${writeFile.name}]")
            FileIOUtils.writeFileFromString(writeFile, content, true)
            deleteFiles(100, logFileList)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(this.javaClass.name, "保存日志失败 error ${e.message}")
        }
    }

    private fun deleteFiles(num: Int, logFileList: ArrayList<File>){
        val fileExt = LogUtils.getConfig().fileExtension
        logFileList?.sortBy{ it.name.replace(fileExt,"").toLong()}
        if(logFileList?.size?:0 > num)logFileList.first()?.delete()
    }

    private fun findRecentLog(logFile: File, sizeInByte: Long, fileExt: String) : File{
        var recentFile = logFile

        try {
            val dirFile = logFile.parentFile
            if(!dirFile.exists()) dirFile.mkdirs()

            val logFiles = dirFile.listFiles()
            val listFiles = ArrayList<File>()
            if(!logFiles.isNullOrEmpty()){
                listFiles.addAll(logFiles)
            }
            listFiles.sortedBy { it.lastModified() }
            if(listFiles.isNullOrEmpty()) return recentFile

            var lastModifyFile = listFiles.last()
            if(lastModifyFile.exists() && (lastModifyFile.length() > sizeInByte)){
               var cnt = listFiles.size
               val logFileExt = "$fileExt"
               val tempPath = "${logFile.absolutePath.removeSuffix(logFileExt)}_${cnt}${logFileExt}"
               val tempFile = File(tempPath)
               if(!tempFile.exists()){
                   tempFile.createNewFile()
               }
               lastModifyFile = tempFile
            }
            recentFile = lastModifyFile
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(this.javaClass.name, "获取最近日志失败 error = ${e.message}")
        }

        return recentFile
    }

    /**
    *  @Des:
    *
    *
    *  @Name:
    *  @Class: LoggerUtils
    *  @Date:   21-1-22  下午2:42
    *  @Param:
    *  @Property: errorCode  错误码  { @link com.whpe.pos.common.ErrorCodeEnum }
    *  @Property: content 详细描述内容
    *  @Exception:
    *  @Return:
    *  @Version: V1.0
    **/
    fun verbose(errorCode: String, content: String){
        try {
            LogUtils.v("${errorCode}, ${content}")
            mIErrorCodeCallback!!.onErrorCode(errorCode)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("","verbose打印失败 error = ${e.message}")
        }
    }

    /**
     *  @Des:
     *
     *
     *  @Name:
     *  @Class: LoggerUtils
     *  @Date:   21-1-22  下午2:42
     *  @Param:
     *  @Property: errorCode  错误码  { @link com.whpe.pos.common.ErrorCodeEnum }  没有对应错误码的情况下，可以传入出错的类名
     *  @Property: content 详细描述内容
     *  @Exception:
     *  @Return:
     *  @Version: V1.0
     **/
    fun debug(errorCode: String, content: String){
        try {
            LogUtils.d("${errorCode}, ${content}")
            mIErrorCodeCallback!!.onErrorCode(errorCode)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("","debug打印失败 error = ${e.message}")
        }
    }

    /**
     *  @Des:
     *
     *
     *  @Name:
     *  @Class: LoggerUtils
     *  @Date:   21-1-22  下午2:42
     *  @Param:
     *  @Property: errorCode  错误码  { @link com.whpe.pos.common.ErrorCodeEnum }  没有对应错误码的情况下，可以传入出错的类名
     *  @Property: content 详细描述内容
     *  @Exception:
     *  @Return:
     *  @Version: V1.0
     **/
    fun info(errorCode: String, content: String){
        try {
            LogUtils.i("${errorCode}, ${content}")
            mIErrorCodeCallback!!.onErrorCode(errorCode)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("","info打印失败 error = ${e.message}")
        }
    }

    /**
     *  @Des:
     *
     *
     *  @Name:
     *  @Class: LoggerUtils
     *  @Date:   21-1-22  下午2:42
     *  @Param:
     *  @Property: errorCode  错误码  { @link com.whpe.pos.common.ErrorCodeEnum }  没有对应错误码的情况下，可以传入出错的类名
     *  @Property: content 详细描述内容
     *  @Exception:
     *  @Return:
     *  @Version: V1.0
     **/
    fun warn(errorCode: String, content: String){
        try {
            LogUtils.w("${errorCode}, ${content}")
            mIErrorCodeCallback!!.onErrorCode(errorCode)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("","warn打印失败 error = ${e.message}")
        }
    }

    /**
     *  @Des:
     *
     *
     *  @Name:
     *  @Class: LoggerUtils
     *  @Date:   21-1-22  下午2:42
     *  @Param:
     *  @Property: errorCode  错误码  { @link com.whpe.pos.common.ErrorCodeEnum }  没有对应错误码的情况下，可以传入出错的类名
     *  @Property: content 详细描述内容
     *  @Exception:
     *  @Return:
     *  @Version: V1.0
     **/
    fun error(errorCode: String, content: String){
        try {
            LogUtils.e("${errorCode}, ${content}")
            mIErrorCodeCallback!!.onErrorCode(errorCode)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("","error打印失败 error = ${e.message}")
        }
    }

    /**
    *  @Des: 打印throwable信息
    *
    *
    *  @Name:
    *  @Class: LoggerUtils
    *  @Date:   21-5-13  上午10:20
    *  @Param:
    *  @Property:
    *  @Exception:
    *  @Return:
    *  @Version: V1.0
    **/
    fun error(throwable : Throwable){
        try {
            val sb = StringBuilder()
            val es = throwable.stackTrace
            sb.append("throwable messsage start\n")
            sb.append("throwable cause [${throwable.cause}]\n")
            sb.append("throwable message [${throwable.message}]\n")
            sb.append("throwable localizedMessage [${throwable.localizedMessage}]\n")
            sb.append("throwable stack :\n")
            es.forEach { action ->
                sb.append("${action.toString()}\n")
            }

            sb.append("throwable messsage end\n")
            LogUtils.e(sb.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("","error打印失败 error = ${e.message}")
        }
    }

    /**
     *  @Des:
     *
     *
     *  @Name:
     *  @Class: LoggerUtils
     *  @Date:   21-1-22  下午2:42
     *  @Param:
     *  @Property: errorCode  错误码  { @link com.whpe.pos.common.ErrorCodeEnum }  没有对应错误码的情况下，可以传入出错的类名
     *  @Property: content 详细描述内容
     *  @Exception:
     *  @Return:
     *  @Version: V1.0
     **/
    fun assert(errorCode: String, content: String){
        try {
            LogUtils.a("${errorCode}, ${content}")
            mIErrorCodeCallback!!.onErrorCode(errorCode)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("","assert打印失败 error = ${e.message}")
        }
    }

}