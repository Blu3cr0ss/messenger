package idk.bluecross.messenger.util.content

import idk.bluecross.messenger.store.entity.FileInDb

class GraphicContent(var image: FileInDb) : Content() {
    override var type = Type.GRAPHIC
}