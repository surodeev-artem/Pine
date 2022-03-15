package artem.surodeev.pine

data class SearchImage(val results: ArrayList<Image>) {
    override fun toString(): String {
        return results.toString()
    }
}
