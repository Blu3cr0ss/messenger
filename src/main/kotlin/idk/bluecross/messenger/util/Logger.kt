package idk.bluecross.messenger.util

import org.apache.commons.logging.LogFactory

fun Any.getLogger() = LogFactory.getLog(this::class.java)