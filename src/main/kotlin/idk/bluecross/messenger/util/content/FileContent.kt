package idk.bluecross.messenger.util.content

import idk.bluecross.messenger.store.entity.FileInDb

class FileContent(var file: FileInDb) : Content() {
    override var type = Type.FILE
}