package idk.bluecross.messenger.store.entity.content

import idk.bluecross.messenger.store.entity.content.Content
import java.util.LinkedList

class ContentTree() : LinkedList<Content>() {
    constructor(vararg contents: Content) : this() {
        addAll(contents)
    }
}