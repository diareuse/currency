package wiki.depasquale.currency.platform.widget

import cursola.rate.FavoriteDataSource
import cursola.rate.LatestValueDataSource
import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.Money
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.math.BigDecimal
import java.util.Currency

class FavoritesSnapshotSourceLatest(
    private val latest: LatestValueDataSource,
    private val favorites: FavoriteDataSource,
    private val converter: CurrencyConverter
) : FavoritesSnapshotSource {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun current(): FavoritesSnapshot {
        val source = Money(latest.value.toBigDecimalOrNull() ?: BigDecimal.ZERO, Currency.getInstance(latest.currency))
        return try {
            snapshotOf(source)
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
            FavoritesSnapshot.Unavailable(source)
        }
    }

    private suspend fun snapshotOf(source: Money): FavoritesSnapshot {
        if (favorites.list().none { it != source.currency }) return FavoritesSnapshot.NoFavorites(source)
        val conversions = converter.convertToFavorites(source)
        if (conversions.isEmpty()) return FavoritesSnapshot.Unavailable(source)
        return FavoritesSnapshot.Converted(source, conversions)
    }

}
