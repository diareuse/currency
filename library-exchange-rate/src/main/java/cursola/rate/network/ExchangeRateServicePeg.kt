package cursola.rate.network

import cursola.rate.ExchangeRate
import java.util.Currency

internal class ExchangeRateServicePeg(
    private val origin: ExchangeRateService
) : ExchangeRateService {

    override suspend fun get() = buildSet {
        for (rate in origin.get()) {
            add(rate)
            addAll(getPegs(rate))
        }
    }.toList()

    private fun getPegs(
        rate: ExchangeRate
    ) = when (rate.currency.currencyCode) {
        "USD" -> getUSDPegs(rate)
        "INR" -> getINRPegs(rate)
        "EUR" -> getEURPegs(rate)
        "SGD" -> getSGDPegs(rate)
        "ZAR" -> getZARPegs(rate)
        "HKD" -> getHKDPegs(rate)
        else -> emptyList()
    }

    // ---

    private fun getUSDPegs(rate: ExchangeRate) = createPegs(
        source = rate,
        pegs = mapOf(
            "BHD" to 0.376,
            "BZD" to 2.0,
            "CUC" to 1.0,
            "DJF" to 177.721,
            "ERN" to 15.0,
            "HKD" to 7.80,
            "JOD" to 0.709,
            "LBP" to 1507.5,
            "OMR" to 0.38449,
            "PAB" to 1.0,
            "QAR" to 3.64,
            "SAR" to 3.75,
            "AED" to 3.6725,
            "AWG" to 1.79,
            "BSD" to 1.0,
            "BBD" to 2.0,
            "BMD" to 1.0,
            "KYD" to 0.83333,
            "XCD" to 2.70,
            "KWD" to 0.29963,
            "MVR" to 12.85, // this one is variable for some reason
            "ANG" to 1.79,
        )
    )

    private fun getINRPegs(rate: ExchangeRate) = createPegs(
        source = rate,
        pegs = mapOf(
            "BTN" to 1.0,
            "NPR" to 1.6,
        )
    )

    private fun getEURPegs(rate: ExchangeRate) = createPegs(
        source = rate,
        pegs = mapOf(
            "BAM" to 1.95583,
            "BGN" to 1.95583,
            "CVE" to 110.265,
            "XAF" to 655.957,
            "XOF" to 655.957,
            "XPF" to 119.33174,
            "KMF" to 491.96775,
            "HRK" to 7.53450,
            "DKK" to 7.46038,
            "MAD" to 10.69,
            "STN" to 24.50,
        )
    )

    private fun getSGDPegs(rate: ExchangeRate) = createPegs(
        source = rate,
        pegs = mapOf("BND" to 1.0)
    )

    private fun getZARPegs(rate: ExchangeRate) = createPegs(
        source = rate,
        pegs = mapOf(
            "LSL" to 1.0,
            "NAD" to 1.0,
            "SZL" to 1.0,
        )
    )

    private fun getHKDPegs(rate: ExchangeRate) = createPegs(
        source = rate,
        pegs = mapOf("MOP" to 1.03)
    )

    // ---

    private fun createPegs(
        source: ExchangeRate,
        pegs: Map<String, Double>
    ) = pegs.map { (key, value) ->
        source.copy(
            currency = Currency.getInstance(key),
            rate = source.rate * value
        )
    }

}