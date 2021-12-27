package com.example.case3.loger

import android.os.Environment
import android.util.Log
import timber.log.Timber
import java.io.File
import java.util.*

/**
 *  系统使用的路径配置信息
 *
 *  @Project: com.tomyang.bus.properties
 *  @Title:
 *  @Package: com.tomyang.bus.properties
 *  @Author  : tomyang
 *  @Date :   2018/7/31  20:43
 *  @Version : V1.1
 *             V1.2 增加sdcard路径
 *             V1.3 新增backup路径，修改数据库备份路径, 新增无文件时自动创建
 **/
class PropertiesPath private constructor(){
    private lateinit var mProperties : Properties
    private lateinit var mPath : String

    private val PATH = "whpe" // android 路径: "/Download/whpe/"
    private val FILENAME = "systempath.xml" //"systempath.xml"

    object Holder{
        val instance =
            PropertiesPath()
    }

    companion object {
        fun getInstance() =
            Holder.instance



        @JvmStatic fun main(args : Array<String>) {
            val prop =
                PropertiesPath()

            println("---> main path =  ${prop.getMainPath()}")
            println("---> voice path =  ${prop.getVoicePath()}")
            println("---> crash path =  ${prop.getcrashPath()}")
            println("---> database path =  ${prop.getDataBaseBackupPath()}")
            println("---> properties path =  ${prop.getPropertiesPath()}")
            println("---> config path =  ${prop.getConfigPath()}")
            println("---> gao path =  ${prop.getGaoPath()}")
            println("---> demo path =  ${prop.getGaoDemoPath()}")
            println("---> demo backup path =  ${prop.getGaoDemoBackupPath()}")
            println("---> demo download path =  ${prop.getGaoDemoDownloadPath()}")
        }
    }

    init {
        reload()
        Log.e("TAG", "PropertiesPath init")
    }

    fun reload(){
        try {
            Timber.tag(PropertiesPath::class.simpleName)

            mPath = PathUtil.concatFilePath(
                getSDCardDownloadPath(),
                PATH
            )  //android环境需要替换为 external sd 卡路径
            mProperties = Properties()
            val file = File(PathUtil.concatFilePath(mPath, FILENAME))

            Log.e("TAG", "---> path properties = ${file.absolutePath}, file exist = ${file.exists()}")
            val inputstream = file.inputStream()
            mProperties.loadFromXML(inputstream)
            inputstream.close()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }
    /**
     *
     * @Desc : 获取证书目录
     *
     * @Name :
     * @Class :
     * @Date : 2018/7/31 20:42
     * @Param :
     * @Property
     * @Return :
     * @Thorws :
     **/
    fun getCertPath() : String{
        var path = "cert"

        try {
            path = PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("cert", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
    *
    * @Desc : 获取外置存储sdcard的根目录路径
    *
    * @Name :
    * @Class : PropertiesPath
    * @Date : 19-11-8 下午2:15
    * @Param :
    * @Property
    * @Return :
    * @Throws :
    **/
    fun getExternalSDCardPath() : String{
        var defaultPath = "/storage/emulated/0"
        try {
            val downloadPathFile = File(getSDCardDownloadPath())
            if(downloadPathFile.exists()){
               defaultPath = downloadPathFile.parentFile.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return defaultPath
    }

    /**
     * 获取android sd卡的Download 共用文件夹路径
     */
    fun getSDCardDownloadPath() : String{
        var path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

//        Timber.d("---> 目录 download = $path")
        return path
    }

    /**
     * 获取主目录
     */
    fun getMainPath() : String{
        var path =  PATH

        try {
            path = mProperties.getProperty("main", path)
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }
        path = PathUtil.concatFilePath(getSDCardDownloadPath(), path)
        return path
    }

    /**
     * 获取语音目录
     */
    fun getVoicePath() : String{
        var path = "voice"

        try {
            path = PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("voice", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
     * 获取奔溃日志保存目录
     */
    fun getcrashPath() : String {
        var path = "crash"

        try {
            path = PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("crash", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }


    /**
    *
    * @Desc : 备份文件夹
    *           V1.3 新增
    * @Name :
    * @Class : PropertiesPath
    * @Date : 19-12-12 上午10:03
    * @Param :
    * @Property
    * @Return :
    * @Throws :
    **/
    fun getBackUpPath() : String{
        var path = "backup"

        try {
            path = PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("backup", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
    *
    * @Desc : 获取数据库备份目录
    *           V1.3 修改
    * @Name :
    * @Class : PropertiesPath
    * @Date : 19-12-12 上午10:04
    * @Param :
    * @Property
    * @Return :
    * @Throws :
    **/
    fun getDataBaseBackupPath() : String {
        var path = "db"

        try {
            path =
                PathUtil.concatFilePath(getBackUpPath(), mProperties.getProperty("database", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
     * 获取系统属性配置目录
     */
    fun getPropertiesPath() : String{
        var path = "properties"

        try {
            path =
                PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("properties", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }


    /**
     * 获取远程配置目录， 例如：交易规则、运行参数就保存在该目录下
     */
    fun getConfigPath() : String{
        var path = "remoteconfig"

        try {
            path = PathUtil.concatFilePath(
                getMainPath(),
                mProperties.getProperty("configfromremote", path)
            )
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }


    /**
     * 获取高国齐 使用的目录，用途不明
     */
    fun getGaoPath() : String{
        var path = "Gao"

        try {
            path = PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("gao", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
     * 获取高国齐 使用的目录，用途不明
     */
    fun getGaoDemoPath() : String{
        var path = "demo"

        try {
            path = PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("demo", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
     * 获取高国齐 使用的目录，用途不明
     */
    fun getGaoDemoBackupPath() : String{
        var path = "demo_bak"

        try {
            path =
                PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("demobackup", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
     * 获取高国齐 使用的目录，用途不明
     */
    fun getGaoDemoDownloadPath() : String{
        var path = "Gao_down"

        try {
            path = PathUtil.concatFilePath(
                getMainPath(),
                mProperties.getProperty("demodownload", path)
            )
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
     *
     * @Desc : 获取 系统运行是使用的参数， 主要用于数据共享等
     *
     *
     * @date : ${DATE} ${TIME}
     * @param :
     * @property
     * @return :
     **/
    fun getRunSharePath() : String{
        var path = "runshare"

        try {
            path = PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("runshare", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
    *
    * @Desc : 获取日志路径，主要用于存储一些运行日志，注意要定期清理，否则文件可能过大过多
    *
    * @Name :
    * @Class : PropertiesPath
    * @Date : 2018/3/8 10:22
    * @Param :
    * @Property
    * @Return :
    * @Thorws :
    **/
    fun getLogPath() : String{
        var path = "log"

        try {
            path = PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("log", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
    *
    * @Desc : 获取app更新日志路径
    *
    * @Name :
    * @Class : PropertiesPath
    * @Date : 20-9-3 上午11:30
    * @Param :
    * @Property
    * @Return :
    * @Throws :
    **/
    fun getAppUpdateLogPath() : String{
        var path = "updateLog"

        try {
            path =
                PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("updateLog", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    fun getFacePath() : String{
        var path = "face"
        try {
            path = PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("face", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
     * @Desc : 获取人脸识别配置文件路径
     * @Author : taojh
     * @Date : 2019-10-24 15:59
     * @Param :
     *
     */
    fun getFaceConfigPath() : String{
        var path = "faceconfig"

        try {
            path =
                PathUtil.concatFilePath(getFacePath(), mProperties.getProperty("faceconfig", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
     * @Desc : 人脸识别证书存放路径
     * @Author : taojh
     * @Date : 2019-10-24 18:36
     * @Param : 
     *
     */
    fun getFaceLicensePath() : String{
        var path = "facelicense"

        try {
            path =
                PathUtil.concatFilePath(getFacePath(), mProperties.getProperty("facelicense", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }


    /**
     * @Desc :
     * @Author : taojh
     * @Date : 2019-10-24 18:36
     * @Param :
     *
     */
    fun getFacePhotoPath() : String{
        var path = "facephoto"

        try {
            path =
                PathUtil.concatFilePath(getFacePath(), mProperties.getProperty("facephoto", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
     * @Desc : 人脸识别模型文件
     * @Author : taojh
     * @Date : 2019-10-24 18:38
     * @Param : 
     *
     */
    fun getFaceModelPath() : String{
        var path = "facemodel"

        try {
            path =
                PathUtil.concatFilePath(getFacePath(), mProperties.getProperty("facemodel", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }

    /**
     * @Desc : 银联需要用到的动态参数
     * @Author : taojh
     * @Date : 2020-01-03 9:12
     * @Param : 
     * @Return : 
     * 
     */
    fun getBankParamPath() : String {
        var path = "bankparam"

        try {
            path = PathUtil.concatFilePath(
                getRunSharePath(),
                mProperties.getProperty("bankparam", path)
            )
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        return PathUtil.concatFilePath(path, "")
    }


    /**
    *
    * @Desc : 根据路径新建所有层级目录
    *
    * @Name :
    * @Class : PropertiesPath
    * @Date : 19-12-12 上午10:10
    * @Param :
    * @Property
    * @Return :
    * @Throws :
    **/
    fun makekdir(path : String){
        try {
            val file = File(path) ?: return

            if(!file.exists()){
                file.mkdirs()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     *  @Des: 获取上传文件目录
     *
     *
     *  @Name:
     *  @Class: PropertiesPath
     *  @Date:   20-12-30  下午4:05
     *  @Param:
     *  @Property:
     *  @Exception:
     *  @Return:
     *  @Version: V1.0
     **/
    fun getUploadFilePath() : String{
        var path = "uploadFile"
        try {
            path =
                PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("uploadFile", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }
        
        return PathUtil.concatFilePath(path, "")
    }
    
    /**
    *  @Des: 获取临时文件夹
    *
    *
    *  @Name:
    *  @Class: PropertiesPath
    *  @Date:   21-2-8  上午10:12
    *  @Param:
    *  @Property:
    *  @Exception:
    *  @Return:
    *  @Version: V1.0
    **/
    fun getTempFilePath() : String{
        var path = "temp"
    
        try {
            path = PathUtil.concatFilePath(getMainPath(), mProperties.getProperty("temp", path))
            makekdir(path)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    
        return PathUtil.concatFilePath(path, "")
    }
}