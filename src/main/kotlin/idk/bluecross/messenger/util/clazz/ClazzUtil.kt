package idk.bluecross.messenger.util.clazz

fun Class<*>.isSubtypeOf(type: Class<*>) = type.isAssignableFrom(this)
fun Class<*>.isSupertypeOf(type: Class<*>) = this.isAssignableFrom(type)
