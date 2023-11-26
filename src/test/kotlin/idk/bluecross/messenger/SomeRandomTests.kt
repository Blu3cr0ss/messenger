package idk.bluecross.messenger

import idk.bluecross.messenger.util.clazz.isSubtypeOf
import idk.bluecross.messenger.util.clazz.isSupertypeOf
import org.junit.jupiter.api.Test

class SomeRandomTests {
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
}