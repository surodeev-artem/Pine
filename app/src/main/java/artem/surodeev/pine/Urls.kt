package artem.surodeev.pine

data class Urls (val small: String, val raw: String) {
    override fun equals(other: Any?): Boolean {
        if(other == null || other !is Urls) {
            return false
        }
        return small == other.small && raw == other.raw
    }
}