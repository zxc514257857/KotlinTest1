package com.example.app.faceobject

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app
 * @Author: zhr
 * @Date: 2021/11/28 18:58
 * @Version: V1.0
 */
class Dog : LuShengAnimal(), ISwim {

    override fun swim() {
        println("swim")
    }
}