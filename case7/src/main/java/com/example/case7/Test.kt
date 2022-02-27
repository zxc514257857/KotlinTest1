package com.example.case7

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case7
 * @Author: zhr
 * @Date: 2022/2/27 22:00
 * @Version: V1.0
 */
class Test {

    var b = ""

    companion object{
        val aa = ""
    }
}

class B{

    init {
        Test().b
        Test.aa
    }
}