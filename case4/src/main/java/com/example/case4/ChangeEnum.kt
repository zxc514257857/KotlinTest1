package com.example.case4

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.case4
 * @Author: zhr
 * @Date: 2021/12/21 11:14
 * @Version: V1.0
 */
enum class ChangeEnum(val value: String) {

    REMOVE_LAST("删除最后"),
    REMOVE_FIRST("删除第一"),
    DEFAULT("不变");

    companion object {
        private val mMap = HashMap<String, ChangeEnum>()

        init {
            values().forEach { enum ->
                mMap[enum.value] = enum
            }
        }

        fun getEnum(value : String) : ChangeEnum {
            return if(mMap.containsKey(value)){
                mMap[value] ?: DEFAULT
            }else{
                DEFAULT
            }
        }
    }
}