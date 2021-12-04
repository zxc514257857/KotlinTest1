package com.example.app.faceobject

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app
 * @Author: zhr
 * @Date: 2021/11/28 18:42
 * @Version: V1.0
 */
class ShuiShengAnimal : Animals(), ISwim {

    override fun eat() {
        println("eat")
    }

    override fun breath() {
        println("breath")
    }

    override fun swim() {
        println("swim")
    }
}