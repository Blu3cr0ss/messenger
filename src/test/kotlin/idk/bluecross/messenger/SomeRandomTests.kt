package idk.bluecross.messenger

import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.lang.reflect.ParameterizedType
import java.util.*

class SomeRandomTests {
    @Test
    fun bytesConversions() {
        val str = "hello!"
        val byteArr = str.toByteArray()
        val b64 =
            "cGx1Z2lucyB7CglpZCAnb3JnLnNwcmluZ2ZyYW1ld29yay5ib290JyB2ZXJzaW9uICczLjEuNScKCWlkICdpby5zcHJpbmcuZGVwZW5kZW5jeS1tYW5hZ2VtZW50JyB2ZXJzaW9uICcxLjEuMycKCWlkICdvcmcuamV0YnJhaW5zLmtvdGxpbi5qdm0nIHZlcnNpb24gJzEuOC4yMicKCWlkICdvcmcuamV0YnJhaW5zLmtvdGxpbi5wbHVnaW4uc3ByaW5nJyB2ZXJzaW9uICcxLjguMjInCn0KCmdyb3VwID0gJ2lkay5ibHVlY3Jvc3MnCnZlcnNpb24gPSAnMC4wLjEtU05BUFNIT1QnCmphdmEuc291cmNlQ29tcGF0aWJpbGl0eSA9ICcxNycKCgpyZXBvc2l0b3JpZXMgewoJbWF2ZW5DZW50cmFsKCkKfQoKZGVwZW5kZW5jaWVzIHsKCWltcGxlbWVudGF0aW9uICdvcmcuc3ByaW5nZnJhbWV3b3JrLmJvb3Q6c3ByaW5nLWJvb3Qtc3RhcnRlci1kYXRhLXIyZGJjJwoJaW1wbGVtZW50YXRpb24gJ29yZy5zcHJpbmdmcmFtZXdvcmsuYm9vdDpzcHJpbmctYm9vdC1zdGFydGVyLXdlYmZsdXgnCglpbXBsZW1lbnRhdGlvbiAnb3JnLnNwcmluZ2ZyYW1ld29yay5ib290OnNwcmluZy1ib290LXN0YXJ0ZXItd2Vic29ja2V0JwoJaW1wbGVtZW50YXRpb24gJ2NvbS5mYXN0ZXJ4bWwuamFja3Nvbi5tb2R1bGU6amFja3Nvbi1tb2R1bGUta290bGluJwoJaW1wbGVtZW50YXRpb24gJ2lvLnByb2plY3RyZWFjdG9yLmtvdGxpbjpyZWFjdG9yLWtvdGxpbi1leHRlbnNpb25zJwoJaW1wbGVtZW50YXRpb24gJ29yZy5qZXRicmFpbnMua290bGluOmtvdGxpbi1yZWZsZWN0JwoJaW1wbGVtZW50YXRpb24gJ29yZy5qZXRicmFpbnMua290bGlueDprb3RsaW54LWNvcm91dGluZXMtcmVhY3RvcicKCWltcGxlbWVudGF0aW9uICdvcmcuZmx5d2F5ZGI6Zmx5d2F5LWNvcmUnCgoJcnVudGltZU9ubHkgJ29yZy5wb3N0Z3Jlc3FsOnBvc3RncmVzcWwnCglydW50aW1lT25seSAnb3JnLnBvc3RncmVzcWw6cjJkYmMtcG9zdGdyZXNxbCcKCgl0ZXN0SW1wbGVtZW50YXRpb24gJ29yZy5zcHJpbmdmcmFtZXdvcmsuYm9vdDpzcHJpbmctYm9vdC1zdGFydGVyLXRlc3QnCgl0ZXN0SW1wbGVtZW50YXRpb24gJ2lvLnByb2plY3RyZWFjdG9yOnJlYWN0b3ItdGVzdCcKfQoKdGVzdCB7Cgl1c2VKVW5pdFBsYXRmb3JtKCkKfQo="

        println(Base64.getDecoder().decode(b64).decodeToString())
    }

    @Test
    fun publishersGenericsConversions() {
        val q = Mono.just("qwe")
        val w = (q as Mono<Any>)
        val e = (w as Mono<String>)

        q.subscribe(::println)
        w.subscribe(::println)
        e.subscribe(::println)
    }

    @Test
    fun `objects - classes conversions`() {
        val s = "rdftgyhjkm"
        val sc = s::class.java
        val so = s as Any

        println()

    }

    @Test
    fun getGenericType() {
        val listOfNumbers = arrayListOf(1, 2, 3)

//        val qwe = (listOfNumbers.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>


    }
}