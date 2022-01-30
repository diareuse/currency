package wiki.depasquale.currency.presentation.view.common

class CoordinateCalculatorFactoryDouble : CoordinateCalculatorFactory<Double> {

    override fun build(metadata: ChartMetadata<Double>): CoordinateCalculator<Double> {
        return CoordinateCalculatorDouble(metadata)
    }
}