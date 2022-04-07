package com.zhr.case8

import com.google.gson.Gson
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
    }

    @Test
    fun test(){
        val collection: Collection<String> = listOf("1", "2", "3")
        val mutableCollection: MutableCollection<String> = arrayListOf("8848")
        val copyElements = copyElements(collection, mutableCollection)
        println("copyElements::${Gson().toJson(copyElements)}")
    }

    private fun <T> copyElements(
        collection: Collection<T>,
        mutableCollection: MutableCollection<T>
    ) : MutableCollection<T> {
        mutableCollection.addAll(collection)
        return mutableCollection
    }
}