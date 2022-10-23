package cursola.rate

import cursola.rate.di.ExchangeRateModule
import cursola.rate.model.makeExchangeRate
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.whenever
import java.util.Currency
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class ExchangeRateDataSourcePeggingTest : AbstractDataSourceTest() {

    private lateinit var source: ExchangeRateDataSource

    override fun prepare() {
        source = ExchangeRateModule.getInstance().exchangeRate(network, database)
    }

    // ---

    @Test
    fun get_returnsPeg_BHD() = runTest {
        preparePeg("USD", 1.0)
        expectValue("BHD", 0.376)
    }

    @Test
    fun get_returnsPeg_BZD() = runTest {
        preparePeg("USD", 1.0)
        expectValue("BZD", 2.0)
    }

    @Test
    fun get_returnsPeg_CUC() = runTest {
        preparePeg("USD", 1.0)
        expectValue("CUC", 1.0)
    }

    @Test
    fun get_returnsPeg_DJF() = runTest {
        preparePeg("USD", 1.0)
        expectValue("DJF", 177.721)
    }

    @Test
    fun get_returnsPeg_ERN() = runTest {
        preparePeg("USD", 1.0)
        expectValue("ERN", 15.0)
    }

    @Test
    fun get_returnsPeg_JOD() = runTest {
        preparePeg("USD", 1.0)
        expectValue("JOD", 0.709)
    }

    @Test
    fun get_returnsPeg_LBP() = runTest {
        preparePeg("USD", 1.0)
        expectValue("LBP", 1507.5)
    }

    @Test
    fun get_returnsPeg_OMR() = runTest {
        preparePeg("USD", 1.0)
        expectValue("OMR", 0.38449)
    }

    @Test
    fun get_returnsPeg_PAB() = runTest {
        preparePeg("USD", 1.0)
        expectValue("PAB", 1.0)
    }

    @Test
    fun get_returnsPeg_QAR() = runTest {
        preparePeg("USD", 1.0)
        expectValue("QAR", 3.64)
    }

    @Test
    fun get_returnsPeg_SAR() = runTest {
        preparePeg("USD", 1.0)
        expectValue("SAR", 3.75)
    }

    @Test
    fun get_returnsPeg_AED() = runTest {
        preparePeg("USD", 1.0)
        expectValue("AED", 3.6725)
    }

    @Test
    fun get_returnsPeg_AWG() = runTest {
        preparePeg("USD", 1.0)
        expectValue("AWG", 1.79)
    }

    @Test
    fun get_returnsPeg_BSD() = runTest {
        preparePeg("USD", 1.0)
        expectValue("BSD", 1.0)
    }

    @Test
    fun get_returnsPeg_BBD() = runTest {
        preparePeg("USD", 1.0)
        expectValue("BBD", 2.0)
    }

    @Test
    fun get_returnsPeg_BMD() = runTest {
        preparePeg("USD", 1.0)
        expectValue("BMD", 1.0)
    }

    @Test
    fun get_returnsPeg_KYD() = runTest {
        preparePeg("USD", 1.0)
        expectValue("KYD", 0.83333)
    }

    @Test
    fun get_returnsPeg_XCD() = runTest {
        preparePeg("USD", 1.0)
        expectValue("XCD", 2.70)
    }

    @Test
    fun get_returnsPeg_KWD() = runTest {
        preparePeg("USD", 1.0)
        expectValue("KWD", 0.29963)
    }

    @Test
    fun get_returnsPeg_MVR() = runTest {
        preparePeg("USD", 1.0)
        expectValue("MVR", 12.85)
    }

    @Test
    fun get_returnsPeg_ANG() = runTest {
        preparePeg("USD", 1.0)
        expectValue("ANG", 1.79)
    }

    @Test
    fun get_returnsPeg_BTN() = runTest {
        preparePeg("INR", 1.0)
        expectValue("BTN", 1.0)
    }

    @Test
    fun get_returnsPeg_NPR() = runTest {
        preparePeg("INR", 1.0)
        expectValue("NPR", 1.6)
    }

    @Test
    fun get_returnsPeg_BAM() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("BAM", 1.95583)
    }

    @Test
    fun get_returnsPeg_BGN() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("BGN", 1.95583)
    }

    @Test
    fun get_returnsPeg_CVE() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("CVE", 110.265)
    }

    @Test
    fun get_returnsPeg_XAF() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("XAF", 655.957)
    }

    @Test
    fun get_returnsPeg_XOF() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("XOF", 655.957)
    }

    @Test
    fun get_returnsPeg_XPF() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("XPF", 119.33174)
    }

    @Test
    fun get_returnsPeg_KMF() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("KMF", 491.96775)
    }

    @Test
    fun get_returnsPeg_HRK() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("HRK", 7.53450)
    }

    @Test
    fun get_returnsPeg_DKK() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("DKK", 7.46038)
    }

    @Test
    fun get_returnsPeg_MAD() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("MAD", 10.69)
    }

    @Test
    fun get_returnsPeg_STN() = runTest {
        preparePeg("EUR", 1.0)
        expectValue("STN", 24.50)
    }

    @Test
    fun get_returnsPeg_BND() = runTest {
        preparePeg("SGD", 1.0)
        expectValue("BND", 1.0)
    }

    @Test
    fun get_returnsPeg_LSL() = runTest {
        preparePeg("ZAR", 1.0)
        expectValue("LSL", 1.0)
    }

    @Test
    fun get_returnsPeg_NAD() = runTest {
        preparePeg("ZAR", 1.0)
        expectValue("NAD", 1.0)
    }

    @Test
    fun get_returnsPeg_SZL() = runTest {
        preparePeg("ZAR", 1.0)
        expectValue("SZL", 1.0)
    }

    @Test
    fun get_returnsPeg_MOP() = runTest {
        preparePeg("HKD", 1.0)
        expectValue("MOP", 1.03)
    }

    // ---

    private suspend fun preparePeg(currency: String, expected: Double = 1.0) {
        val value = makeExchangeRate(currency = Currency.getInstance(currency), rate = expected)
        whenever(network.get()).thenReturn(listOf(value))
    }

    private suspend fun expectValue(currency: String, value: Double) {
        val rates = source.get()
        val rate = rates.firstOrNull { it.currency.currencyCode == currency }
        assertNotNull(rate, "Cannot find $currency in $rates")
        assertEquals(value, rate.rate)
    }

}