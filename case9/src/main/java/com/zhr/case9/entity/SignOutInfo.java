package com.zhr.case9.entity;

import android.text.format.DateUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @Des:
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case9.entity
 * @Author: zhr
 * @Date: 2022/4/2 10:18
 * @Version: V1.0
 */

// Greendao entity类 必须是用java 代码创建
// 这个类创建完之后 build 然后会自动生成 Master类、Session类以及Dao类
@Entity
public class SignOutInfo {

    @Id(autoincrement = true)
    public Long id;

    public String shiftId;
    public String signOutTime;
    public boolean isForceSignOut;
    public boolean isUpload;

    public SignOutInfo(String shiftId, boolean isForceSignOut) {
        this.shiftId = shiftId;
        this.signOutTime = "20220402";
        this.isForceSignOut = isForceSignOut;
        this.isUpload = false;
    }

    @Generated(hash = 1058188261)
    public SignOutInfo(Long id, String shiftId, String signOutTime,
                       boolean isForceSignOut, boolean isUpload) {
        this.id = id;
        this.shiftId = shiftId;
        this.signOutTime = signOutTime;
        this.isForceSignOut = isForceSignOut;
        this.isUpload = isUpload;
    }

    @Generated(hash = 1827171551)
    public SignOutInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShiftId() {
        return this.shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getSignOutTime() {
        return this.signOutTime;
    }

    public void setSignOutTime(String signOutTime) {
        this.signOutTime = signOutTime;
    }

    public boolean getIsForceSignOut() {
        return this.isForceSignOut;
    }

    public void setIsForceSignOut(boolean isForceSignOut) {
        this.isForceSignOut = isForceSignOut;
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }
}