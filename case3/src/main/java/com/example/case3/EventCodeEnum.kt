package com.example.case3

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case2
 * @Author: zhr
 * @Date: 2022/1/10 10:52
 * @Version: V1.0
 */
enum class EventCodeEnum(val code: Int, val msg: String) {

    DEFAULT(0x00, "不变"),
    CODE1(0x01, "code1"),
    CODE2(0x02, "code2");

    companion object {
        private val mMap = HashMap<Int, EventCodeEnum>()

        init {
            values().forEach { enum ->
                mMap[enum.code] = enum
            }
        }

        fun getEnum(code: Int): EventCodeEnum {
            return if (mMap.containsKey(code)) {
                mMap[code] ?: DEFAULT
            } else {
                DEFAULT
            }
        }
    }
}