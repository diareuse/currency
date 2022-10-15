package cursola.core.util

import java.util.Currency

fun nextCurrency(): Currency =
    Currency.getAvailableCurrencies().random()