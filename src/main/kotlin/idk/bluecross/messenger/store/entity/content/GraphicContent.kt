package idk.bluecross.messenger.store.entity.content

import idk.bluecross.messenger.store.entity.FileInDb

class GraphicContent(var image: FileInDb) : Content() {
    override var type = Type.GRAPHIC
}