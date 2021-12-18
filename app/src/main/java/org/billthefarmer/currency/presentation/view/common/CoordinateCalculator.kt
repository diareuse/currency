package org.billthefarmer.currency.presentation.view.common

interface CoordinateCalculator<Sample> {

    val strideX: Float

    fun getX(sample: Sample, index: Int): Float
    fun getY(sample: Sample, index: Int): Float

}