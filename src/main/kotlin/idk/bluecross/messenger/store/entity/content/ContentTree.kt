package idk.bluecross.messenger.store.entity.content

import java.util.*

class ContentTree() : LinkedList<Content>() {
    constructor(vararg contents: Content) : this() {
        addAll(contents)
    }
}