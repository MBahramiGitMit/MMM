package ir.mehdibahrami.mbmovie.paging

interface Paginator<Key,Item> {
    suspend fun loadNextItems()
    fun reset()
}