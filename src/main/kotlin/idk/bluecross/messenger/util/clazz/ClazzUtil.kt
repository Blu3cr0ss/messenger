package idk.bluecross.messenger.util.clazz

import idk.bluecross.messenger.MessengerApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors


fun Class<*>.isSubtypeOf(type: Class<*>) = type.isAssignableFrom(this)
fun Class<*>.isSupertypeOf(type: Class<*>) = this.isAssignableFrom(type)

val packages =
    MessengerApplication::class.java.classLoader.definedPackages.filter { it.name.startsWith("idk.bluecross.messenger.store.entity") }
val names =
    packages.flatMap { findAllClassesUsingClassLoader(it.name) }.associate { it.simpleName.decapitalize() to it }

fun findAllClassesUsingClassLoader(packageName: String): Set<Class<*>> {
    val stream = ClassLoader.getSystemClassLoader()
        .getResourceAsStream(packageName.replace(".", "/"))
    val reader = BufferedReader(InputStreamReader(stream))
    return reader.lines()
        .filter { line: String -> line.endsWith(".class") }
        .map { line: String -> getClass(line, packageName) }
        .collect(Collectors.toSet())
}

private fun getClass(className: String, packageName: String): Class<*> {
    return Class.forName(
        packageName + "."
                + className.substring(0, className.lastIndexOf('.'))
    )
}

fun getClassFromCollectionName(collection: String) = names[collection]
