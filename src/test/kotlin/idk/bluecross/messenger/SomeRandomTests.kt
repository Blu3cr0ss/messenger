package idk.bluecross.messenger

import idk.bluecross.messenger.util.clazz.isSubtypeOf
import idk.bluecross.messenger.util.clazz.isSupertypeOf
import org.junit.jupiter.api.Test
import org.springframework.core.KotlinDetector

class SomeRandomTests {
    @Test
    fun calc() {
        println(10175 / 200)
    }
    @Test
    fun clazzSaving() {
        val q = "qwe"
        val w = q as Any

        println(w::class.java)
    }

    @Test
    fun listIsIterable() {
        println(arrayListOf(1).javaClass.isSubtypeOf(Iterable::class.java))
        println(Iterable::class.java.isSupertypeOf(arrayListOf(1)::class.java))
    }

    @Test
    fun bug() {
        val arr1: List<Int?> = listOf(null, 1)     // assume this is typical nullable java list
        val arr2: List<Int> = arr1 as List<Int>    // and we want to convert it to non nullable kotlin array
        val arr3 = arr1.toList()
        // I just found out that toList() converts generic to non-nullable type
        // (arr3 is List<Int> if you don't explicitly specify that it is List<Int?>
        // (just like the list it was converted from).
        // I'm not sure if this is how it should work, but I'm not sure otherwise,
        // so I decided to ask here instead of creating a new issue.

        println(arr3)                              // output: [null, 1]
        println(arr2)                              // output: [null, 1]
        // Array somehow contains null
    }

    @Test
    fun AnyInListIsSubOf() {
        val q = ""
        val w: List<Any> = listOf(q)
        w.forEach {
            println(it::class.java.isSubtypeOf(String::class.java)) //true
        }
    }

    @Test
    fun isKotlinPresent(){
        println(KotlinDetector.isKotlinPresent())
    }
}