package idk.bluecross.messenger.util.content

import idk.bluecross.messenger.store.entity.FileInDb
import idk.bluecross.messenger.util.db.MonoDocRef

class GraphicContent(var image: MonoDocRef<FileInDb>) : Content() {
    override var type = Type.GRAPHIC
}