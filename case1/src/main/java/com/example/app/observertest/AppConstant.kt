package com.example.app.observertest

/**
 * @Des:
 *
 * @Title:
 * @Project: KotlinTest1
 * @Package: com.example.app.observertest
 * @Author: zhr
 * @Date: 2021/11/30 14:39
 * @Version: V1.0
 */
class AppConstant {

    interface EventCode {

        companion object {
            const val MAIN_POST: Int = 0x01
            const val ASYNC_POST: Int = 0x02

            const val HANDLER_POST1: Int = 0x03
            const val HANDLER_POST2: Int = 0x04

            const val TEST1: Int = 0x05
        }
    }
}