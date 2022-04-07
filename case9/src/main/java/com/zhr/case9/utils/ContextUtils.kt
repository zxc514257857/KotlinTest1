package com.zhr.case9.utils

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.Utils
import java.lang.ref.SoftReference

/**
 * @Des:
 *
 *
 *
 * @Title: ContextUtils
 * @Project: FourInOne_ZhenJiang
 * @Package: com.whpe.pos.common.utils
 * @Author: symb
 * @Date: 2021年11月11日 14:17
 * @Version:V1.0
 */
object ContextUtils {

    var context: Application = Utils.getApp()
        private set

    fun getSoftReferenceContext(): SoftReference<Context> {
        return SoftReference<Context>(context)
    }
}