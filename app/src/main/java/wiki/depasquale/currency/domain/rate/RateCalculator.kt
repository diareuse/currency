package wiki.depasquale.currency.domain.rate

fun interface RateCalculator {

    fun getAdjustedRate(rate: Double): Double

}