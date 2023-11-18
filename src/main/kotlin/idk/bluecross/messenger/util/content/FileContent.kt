package idk.bluecross.messenger.util.content

import idk.bluecross.messenger.store.entity.FileInDb
import idk.bluecross.messenger.util.db.MonoDocRef
import java.io.File

class FileContent(var file: MonoDocRef<FileInDb>) : Content() {
    override var type = Type.FILE
}