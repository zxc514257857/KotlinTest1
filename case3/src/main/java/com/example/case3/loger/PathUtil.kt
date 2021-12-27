package com.example.case3.loger

/**
 *  文件路径工具
 *
 *  @Project: com.tomyang.bus.com.whpe.pos.common.utils
 *  @Title:
 *  @Package: com.tomyang.bus.com.whpe.pos.common.utils
 *  @Author  : tomyang
 *  @Date :   19-10-14  上午10:28
 *  @Version : V1.0
 **/
class PathUtil{

    companion object {

        private val SPLASH : String = try {
            System.getProperty("file.separator")?:""
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
        fun concatFilePath(path : String, filename : String) : String{
            var dir = path
            var absolutePath = ""

            if(dir.endsWith(SPLASH)) absolutePath = dir + filename
            else absolutePath = dir + SPLASH + filename

            return absolutePath
        }
    
    
        fun concatFilePath(vararg path :String) : String{
            var dir = path.first()
        
            path.toList().subList(1, path.size).iterator().forEach {
                if(it.endsWith(SPLASH)) dir += it
                else dir = dir + SPLASH + it
            }
        
            return dir
        }
    }
}