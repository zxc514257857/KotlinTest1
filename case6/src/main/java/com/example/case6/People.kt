package com.example.case6

import com.google.gson.annotations.Expose

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case6
 * @Author: zhr
 * @Date: 2022/1/20 11:05
 * @Version: V1.0
 */
class People {

    @Expose
    var name: String? = null
    @Expose(deserialize = false)
    var age: Int? = null
    @Expose(serialize = false)
    var sex: String? = null
    @Transient
    var country: String? = null
}