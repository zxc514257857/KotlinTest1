package com.zhr.case8

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.zhr.case8
 * @Author: zhr
 * @Date: 2022/3/7 10:05
 * @Version: V1.0
 */
class BaseEvent<T> {

    var code: Int? = null
    var data: T? = null

    constructor(code: Int?) {
        this.code = code
    }

    constructor(code: Int?, data: T?) {
        this.code = code
        this.data = data
    }
}