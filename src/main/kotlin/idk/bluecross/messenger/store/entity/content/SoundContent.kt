package idk.bluecross.messenger.store.entity.content

import idk.bluecross.messenger.store.entity.FileInDb

class SoundContent(var sound: FileInDb) : Content() {
    override var type = Type.SOUND
    override fun getContent() = sound
}