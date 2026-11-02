package com.kanyarat.gamedev

const val EXTRA_INDEX = "item_index"

data class GameItem(val id: String, val name: String)

object RemoteUrls {
    private const val ROOT = "https://raw.githubusercontent.com/winterzmr/gamedev/main/assets"
    private const val BASE = "$ROOT/assignment6"
    private const val GALLERY_BASE = "$ROOT/assignment7"

    const val GALLERY_COUNT = 10

    val items: List<GameItem> = listOf(
        GameItem("item01", "ดาบเหล็ก"),
        GameItem("item02", "โล่ไม้"),
        GameItem("item03", "ยาฟื้นพลัง"),
        GameItem("item04", "กุญแจทอง"),
        GameItem("item05", "คทาเวทมนตร์"),
        GameItem("item06", "หมวกนักรบ"),
        GameItem("item07", "ธนูป่า"),
        GameItem("item08", "แหวนมนตรา"),
        GameItem("item09", "ลูกแก้วคริสตัล"),
        GameItem("item10", "เหรียญทอง")
    )

    fun imageUrl(item: GameItem): String = "$BASE/images/${item.id}.png"

    fun textUrl(item: GameItem): String = "$BASE/texts/${item.id}.txt"

    fun galleryImageUrl(index: Int): String =
        "$GALLERY_BASE/images/photo${(index + 1).toString().padStart(2, '0')}.png"
}
