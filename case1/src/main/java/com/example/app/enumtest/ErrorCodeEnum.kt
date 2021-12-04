package com.example.app.enumtest

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.enumtest
 * @Author: zhr
 * @Date: 2021/11/30 16:54
 * @Version: V1.0
 */
enum class ErrorCodeEnum(val code: Int, val msg: String) {

    DEVICE_NOT_EXIST(1000, "设备不存在"),
    DEVICE_BEEP_FAILED(1001, "蜂鸣器操作失败"),
    DEVICE_UART_FAILED(1002, "串口通讯失败");

    companion object {
        // 创建一个map集合 根据错误码去获取获取枚举对象
        private val mMap = HashMap<Int, ErrorCodeEnum>()

        init {
            // 将Map中的数据填满
            values().forEach { enum ->
                mMap[enum.code] = enum
            }
        }

        // 根据 key获取枚举对象
        fun getEnum(code: Int): ErrorCodeEnum {
            return if (mMap.containsKey(code)) {
                mMap[code] ?: throw IllegalArgumentException("错误码 $code 不存在")
            } else {
                throw IllegalArgumentException("错误码 $code 不存在")
            }
        }
    }
}