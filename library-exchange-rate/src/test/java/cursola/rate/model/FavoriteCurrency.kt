package cursola.rate.model

import cursola.rate.database.FavoriteCurrency
import java.util.Currency

internal fun makeFavorite(
    currency: Currency,
    priority: Int = 0
) = FavoriteCurrency(
    currency = currency,
    priority = priority
)