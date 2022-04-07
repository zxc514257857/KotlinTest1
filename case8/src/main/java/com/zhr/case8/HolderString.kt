package com.zhr.case8

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case8
 * @Author: zhr
 * @Date: 2022/3/29 15:44
 * @Version: V1.0
 */
class HolderString : HolderWrapper<String> {

    var data = ""

    override fun getValue(): String {
        return data
    }

    override fun setValue(t: String) {
        data = t
    }
}