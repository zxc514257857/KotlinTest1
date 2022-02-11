package com.example.case6

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case6
 * @Author: zhr
 * @Date: 2022/1/19 10:31
 * @Version: V1.0
 */
object JsonUtils {

    val gson = Gson()

    // let就类似于是建造者模式
    val gsonExpose = GsonBuilder().let {
        // 排除带有以下修饰符的属性  it.excludeFieldsWithModifiers
        // 排除没有以下修饰符的属性  it.excludeFieldsWithoutExposeAnnotation
        it.excludeFieldsWithoutExposeAnnotation()
        it.create()
    }

    fun toJson(any: Any): String {
        return gson.toJson(any)
    }

    fun toJson1(any: Any): String {
        return gsonExpose.toJson(any)
    }

    // 这样写手动用Class来指定泛型，一定没有问题，但是否有必要这样写
    fun <T> parseObject(jsonStr: String, typeOfT: Type): T? {
        return gson.fromJson<T>(jsonStr, typeOfT)
    }

    // 有可能会导致泛型丢失问题
    fun <T> parseObject1(jsonStr: String): T? {
        return gson.fromJson<T>(jsonStr, object : TypeToken<T>() {}.type)
    }

    // 加上 reified 避免泛型丢失
    inline fun <reified T> parseObject2(jsonStr: String): T? {
        return gson.fromJson<T>(jsonStr, object : TypeToken<T>() {}.type)
    }

    fun <T> parseObject3(jsonStr: String, typeOfT: Type): T? {
        return gsonExpose.fromJson<T>(jsonStr, typeOfT)
    }
}