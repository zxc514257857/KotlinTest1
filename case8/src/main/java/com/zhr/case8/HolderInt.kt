package com.zhr.case8

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case8
 * @Author: zhr
 * @Date: 2022/3/29 11:37
 * @Version: V1.0
 */
class HolderInt : Holder<Int> {

    var a: Int = 0

    override fun getValue(): Int = a

    override fun setValue(value: Int) {
        a = value
    }
}