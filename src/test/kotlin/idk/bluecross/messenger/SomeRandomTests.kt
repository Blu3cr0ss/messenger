package idk.bluecross.messenger

import org.junit.jupiter.api.Test

class SomeRandomTests {
    @Test
    fun clazzSaving(){
        val q = "qwe" // STRING
        val w = q as Any // ANY

        println(w::class.java)
    }
}