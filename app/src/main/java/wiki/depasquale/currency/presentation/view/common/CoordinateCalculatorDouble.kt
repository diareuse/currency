package wiki.depasquale.currency.presentation.view.common

class CoordinateCalculatorDouble(
    private val metadata: ChartMetadata<Double>
) : CoordinateCalculator<Double> {

    override val strideX: Float = metadata.container.width / (metadata.count - 1)

    override fun getX(sample: Double, index: Int): Float {
        return index * strideX + metadata.container.left
    }

    override fun getY(sample: Double, index: Int): Float {
        val min = metadata.minY
        val max = metadata.maxY
        val heightPercentage = (1 - ((sample - min) / (max - min))).toFloat()
        return metadata.container.height * heightPercentage
    }

}