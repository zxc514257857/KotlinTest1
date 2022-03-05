package com.example.officialaccounttest

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @Des:
 *
 * @Title:
 * @Project:
 * @Package:
 * @Author: zhr
 * @Date: 2021/11/21 22:56
 * @Version:V1.0
 */
@JsonClass(generateAdapter = true)
data class Person1(
    // 字段改名
    @Json(name = "name")
    val name111: String = "zhr",
    val age: Int = 18,
    val hobby: MutableList<String> = arrayListOf("run", "swim"),
    val friend: Array<String> = arrayOf("xiaohong","xiaohuang")
)