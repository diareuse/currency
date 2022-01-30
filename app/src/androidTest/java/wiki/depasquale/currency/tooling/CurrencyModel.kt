package wiki.depasquale.currency.tooling

import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.presentation.model.CurrencyModel
import java.util.*
import kotlin.random.Random

@OptIn(ExperimentalStdlibApi::class)
fun getCurrencies(count: Int): List<CurrencyModel> = buildList {
    repeat(count) {
        this += getModel()
    }
}

private fun getModel() =
    CurrencyModel(getRate())

private fun getRate(): ExchangeRate {
    return ExchangeRate(CurrencyPool.random(), Random.nextDouble(0.0, Double.MAX_VALUE), Date())
}