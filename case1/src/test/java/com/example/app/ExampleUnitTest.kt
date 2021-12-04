package com.example.app

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

        // 在androidstudio中运行java代码，可以在test类中进行
        TestJava().testPrint()
        println("123666".subStringAndDefault("883399"))
        println(IntGenerator().generate(3))
    }
}