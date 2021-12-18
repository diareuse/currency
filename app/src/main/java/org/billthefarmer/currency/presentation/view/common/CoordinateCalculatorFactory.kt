package org.billthefarmer.currency.presentation.view.common

interface CoordinateCalculatorFactory<Sample> {

    fun build(metadata: ChartMetadata<Sample>): CoordinateCalculator<Sample>

}