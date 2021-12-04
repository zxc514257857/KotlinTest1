package com.example.app.enumtest

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.enumtest
 * @Author: zhr
 * @Date: 2021/11/30 17:53
 * @Version: V1.0
 */
enum class CityEnum(val cityName: String) {

    WUHAN("wuhan"),
    XIANGYANG("xiangyang"),
    HANGZHOU("hangzhou");

    companion object {
        private val mMap = HashMap<String, CityEnum>()

        init {
            values().forEach { enum ->
                mMap[enum.cityName] = enum
            }
        }

        fun getEnum(cityName: String): CityEnum {
            return if (mMap.containsKey(cityName)) {
                mMap[cityName] ?: WUHAN
            } else {
                WUHAN
            }
        }
    }
}