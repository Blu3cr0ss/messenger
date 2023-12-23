package idk.bluecross.messenger.store.entity.content

import idk.bluecross.messenger.store.entity.FileInDb

class VideoContent(var video: FileInDb) : Content() {
    override var type = Type.VIDEO
    override fun getContent() = video
}