package com.example.case6

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case6
 * @Author: zhr
 * @Date: 2022/1/18 22:51
 * @Version: V1.0
 */
class Person {

    var name: String? = null
    var age: Int? = null
    var country: String? = null
        get() = "CN"

    override fun toString(): String {
        return "Person(name=$name, age=$age, country=$country)"
    }
}