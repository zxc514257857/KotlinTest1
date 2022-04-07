package com.zhr.case8

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case8
 * @Author: zhr
 * @Date: 2022/3/29 11:36
 * @Version: V1.0
 */
interface Holder<T> {

    fun getValue(): T
    fun setValue(t: T)
}