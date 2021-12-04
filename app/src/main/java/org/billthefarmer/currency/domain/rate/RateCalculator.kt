package org.billthefarmer.currency.domain.rate

fun interface RateCalculator {

    fun getAdjustedRate(rate: Double): Double

}