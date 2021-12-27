package com.example.case3.loger

import androidx.annotation.IntDef

@IntDef( *[LoggerUtils.VERBOSE, LoggerUtils.DEBUG, LoggerUtils.INFO, LoggerUtils.WARN, LoggerUtils.ERROR, LoggerUtils.ASSERT])
@Retention(AnnotationRetention.SOURCE)
annotation class LogType(){

}

