package com.zhr.case8

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case8
 * @Author: zhr
 * @Date: 2022/3/31 14:05
 * @Version: V1.0
 */
class HolderInt1 : HolderWrapper<Int> {

    var a: Int = 0

    override fun getValue(): Int = a

    override fun setValue(value: Int) {
        a = value
    }
}