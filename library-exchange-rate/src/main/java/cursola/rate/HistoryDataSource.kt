package cursola.rate

import java.util.Currency

interface HistoryDataSource {

    suspend fun get(currency: Currency): List<ExchangeRate>

}