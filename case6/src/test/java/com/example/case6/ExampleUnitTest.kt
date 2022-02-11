package com.example.case6

import android.util.Log
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        test()
    }

    private fun test() {
        val aa = mutableListOf<Any>()
        aa.apply {
            add(100)
            add("哈哈")
            add(32.05)
        }
        println("onCreate: $aa")
    }
}