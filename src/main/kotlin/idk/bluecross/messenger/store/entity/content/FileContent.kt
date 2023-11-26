package idk.bluecross.messenger.store.entity.content

import idk.bluecross.messenger.store.entity.FileInDb

class FileContent(var file: FileInDb) : Content() {
    override var type = Type.FILE
}