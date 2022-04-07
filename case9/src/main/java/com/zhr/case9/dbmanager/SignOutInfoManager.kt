package com.zhr.case9.dbmanager

import com.zhr.case9.entity.SignOutInfo
import com.zhr.case9.gen.SignOutInfoDao

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case9.dbmanager
 * @Author: zhr
 * @Date: 2022/4/2 14:35
 * @Version: V1.0
 */
object SignOutInfoManager {

    fun getNoUploadSignOutInfo(): SignOutInfo? {
        return Case9DbManager.daoSession?.signOutInfoDao?.queryBuilder()
            ?.where(SignOutInfoDao.Properties.IsUpload.eq(false))
            ?.orderAsc(SignOutInfoDao.Properties.Id)?.build()
            ?.forCurrentThread()?.list()?.firstOrNull()
    }

    fun saveSignOutInfo(shiftId: String?, isForceSignOut: Boolean): Boolean {
        var result = false
        try {
            Case9DbManager.daoSession?.signOutInfoDao?.insert(
                SignOutInfo(
                    shiftId,
                    isForceSignOut
                )
            )?.let {
                result = it > 0L
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}