package idk.bluecross.messenger.util.spring

import idk.bluecross.messenger.MessengerApplication
import org.springframework.context.ApplicationContext

object Beans {
    fun <T> getBean(clazz: Class<T>): T = MessengerApplication.appCtx.getBean(clazz)
}