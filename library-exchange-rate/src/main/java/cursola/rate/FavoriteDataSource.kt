package cursola.rate

import java.util.Currency

interface FavoriteDataSource {

    suspend fun list(): List<Currency>

    suspend fun add(currency: Currency, priority: Int)
    suspend fun remove(currency: Currency)

}