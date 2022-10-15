package cursola.network.util

import java.util.Currency

fun nextCurrency(): Currency =
    Currency.getAvailableCurrencies().random()