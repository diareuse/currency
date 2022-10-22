package cursola.rate

import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.database.FavoriteCurrency
import java.util.Currency

internal class FavoriteDataSourceDatabase(
    private val database: ExchangeRateDatabase
) : FavoriteDataSource {

    override suspend fun list(): List<Currency> {
        return database.favorites().get().map(FavoriteCurrency::currency)
    }

    override suspend fun add(currency: Currency, priority: Int) {
        val favorites = database.favorites()
        val favorite = FavoriteCurrency(currency, priority)
        try {
            favorites.insert(favorite)
        } catch (e: Throwable) {
            favorites.update(favorite)
        }
    }

    override suspend fun remove(currency: Currency) {
        database.favorites().delete(FavoriteCurrency(currency, 0))
    }

}