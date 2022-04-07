package com.zhr.case8

import android.content.Context
import android.util.Log
import xcrash.ICrashCallback
import xcrash.XCrash
import java.lang.ref.SoftReference

object xCrashUtil {

    fun init(softCtx: SoftReference<Context>, dir: String) {
        if (null == softCtx.get()) return

        val callback =
            ICrashCallback { logPath, emergency ->
                Log.d(
                    "CrashUtil",
                    "xcarsh info = $logPath, $emergency"
                )
            }

        val parameter = XCrash.InitParameters()
        parameter.enableAnrCrashHandler()
            .enableJavaCrashHandler()
            .enableNativeCrashHandler()
            .setAppVersion(getVersion(softCtx.get()!!))
            .setLogDir(dir)
            .setAnrCallback(callback)
            .setJavaCallback(callback)
            .setNativeCallback(callback)
            .setJavaRethrow(true)
            .setAnrRethrow(true)
            .setNativeRethrow(true)

        XCrash.init(softCtx.get(), parameter)
    }


    private fun getVersion(ctx: Context): String {
        return try {
            val packageName = ctx.packageName ?: ""
            val packageManager = ctx.packageManager
            packageManager?.getPackageInfo(packageName, 0)?.versionName ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}