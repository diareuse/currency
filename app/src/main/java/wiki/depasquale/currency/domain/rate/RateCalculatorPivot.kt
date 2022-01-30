package wiki.depasquale.currency.domain.rate

import wiki.depasquale.currency.domain.model.ExchangeRate

class RateCalculatorPivot(
    private val pivot: ExchangeRate?,
    private val value: Double
) : RateCalculator {

    override fun getAdjustedRate(rate: Double): Double {
        if (pivot == null) {
            return rate
        }

        return value / pivot.rate * rate
    }

}