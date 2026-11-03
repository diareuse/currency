package wiki.depasquale.currency.platform.widget

fun interface FavoritesSnapshotSource {

    suspend fun current(): FavoritesSnapshot

}
