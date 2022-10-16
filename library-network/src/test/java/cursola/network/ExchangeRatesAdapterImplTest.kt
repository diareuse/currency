package cursola.network

import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test
import org.xmlpull.v1.XmlPullParserFactory
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ExchangeRatesAdapterImplTest {

    private lateinit var adapter: ExchangeRatesAdapterImpl

    @Before
    fun prepare() {
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        adapter = ExchangeRatesAdapterImpl(parser, formatter)
    }

    @Test
    fun adapt_returns_unorderedElements() {
        val result = adapter.adapt(mockResponse)
        val expected = singleResponse

        assertEquals(
            expected.size,
            result.size,
            "List size doesn't match, expected ${expected.size} but was ${result.size}"
        )
        for (element in result) {
            assertContains(expected, element)
        }
    }

    companion object {

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            .parse("2021-11-12")
            .let { requireNotNull(it) }
        val singleResponse = listOf(
            ExchangeRateResponse(Currency.getInstance("USD"), 1.1448, date),
            ExchangeRateResponse(Currency.getInstance("JPY"), 130.50, date),
            ExchangeRateResponse(Currency.getInstance("BGN"), 1.9558, date),
            ExchangeRateResponse(Currency.getInstance("CZK"), 25.238, date),
            ExchangeRateResponse(Currency.getInstance("DKK"), 7.4370, date),
            ExchangeRateResponse(Currency.getInstance("GBP"), 0.85505, date),
            ExchangeRateResponse(Currency.getInstance("HUF"), 366.15, date),
            ExchangeRateResponse(Currency.getInstance("PLN"), 4.6428, date),
            ExchangeRateResponse(Currency.getInstance("RON"), 4.9488, date),
            ExchangeRateResponse(Currency.getInstance("SEK"), 10.0085, date),
            ExchangeRateResponse(Currency.getInstance("CHF"), 1.0568, date),
            ExchangeRateResponse(Currency.getInstance("ISK"), 150.40, date),
            ExchangeRateResponse(Currency.getInstance("NOK"), 9.9508, date),
            ExchangeRateResponse(Currency.getInstance("HRK"), 7.5123, date),
            ExchangeRateResponse(Currency.getInstance("RUB"), 82.6649, date),
            ExchangeRateResponse(Currency.getInstance("TRY"), 11.3987, date),
            ExchangeRateResponse(Currency.getInstance("AUD"), 1.5690, date),
            ExchangeRateResponse(Currency.getInstance("BRL"), 6.1902, date),
            ExchangeRateResponse(Currency.getInstance("CAD"), 1.4416, date),
            ExchangeRateResponse(Currency.getInstance("CNY"), 7.3047, date),
            ExchangeRateResponse(Currency.getInstance("HKD"), 8.9206, date),
            ExchangeRateResponse(Currency.getInstance("IDR"), 16239.91, date),
            ExchangeRateResponse(Currency.getInstance("ILS"), 3.5600, date),
            ExchangeRateResponse(Currency.getInstance("INR"), 85.1930, date),
            ExchangeRateResponse(Currency.getInstance("KRW"), 1349.10, date),
            ExchangeRateResponse(Currency.getInstance("MXN"), 23.6472, date),
            ExchangeRateResponse(Currency.getInstance("MYR"), 4.7692, date),
            ExchangeRateResponse(Currency.getInstance("NZD"), 1.6293, date),
            ExchangeRateResponse(Currency.getInstance("PHP"), 56.995, date),
            ExchangeRateResponse(Currency.getInstance("SGD"), 1.5494, date),
            ExchangeRateResponse(Currency.getInstance("THB"), 37.527, date),
            ExchangeRateResponse(Currency.getInstance("ZAR"), 17.4919, date),
        )

        @Language("xml")
        val mockResponseString = """<?xml version="1.0" encoding="UTF-8"?>
            <gesmes:Envelope
              xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01"
              xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
              <gesmes:subject>Reference rates</gesmes:subject>
              <gesmes:Sender>
                <gesmes:name>European Central Bank</gesmes:name>
              </gesmes:Sender>
              <Cube>
                <Cube time='2021-11-12'>
                  <Cube currency='USD' rate='1.1448'/>
                  <Cube currency='JPY' rate='130.50'/>
                  <Cube currency='BGN' rate='1.9558'/>
                  <Cube currency='CZK' rate='25.238'/>
                  <Cube currency='DKK' rate='7.4370'/>
                  <Cube currency='GBP' rate='0.85505'/>
                  <Cube currency='HUF' rate='366.15'/>
                  <Cube currency='PLN' rate='4.6428'/>
                  <Cube currency='RON' rate='4.9488'/>
                  <Cube currency='SEK' rate='10.0085'/>
                  <Cube currency='CHF' rate='1.0568'/>
                  <Cube currency='ISK' rate='150.40'/>
                  <Cube currency='NOK' rate='9.9508'/>
                  <Cube currency='HRK' rate='7.5123'/>
                  <Cube currency='RUB' rate='82.6649'/>
                  <Cube currency='TRY' rate='11.3987'/>
                  <Cube currency='AUD' rate='1.5690'/>
                  <Cube currency='BRL' rate='6.1902'/>
                  <Cube currency='CAD' rate='1.4416'/>
                  <Cube currency='CNY' rate='7.3047'/>
                  <Cube currency='HKD' rate='8.9206'/>
                  <Cube currency='IDR' rate='16239.91'/>
                  <Cube currency='ILS' rate='3.5600'/>
                  <Cube currency='INR' rate='85.1930'/>
                  <Cube currency='KRW' rate='1349.10'/>
                  <Cube currency='MXN' rate='23.6472'/>
                  <Cube currency='MYR' rate='4.7692'/>
                  <Cube currency='NZD' rate='1.6293'/>
                  <Cube currency='PHP' rate='56.995'/>
                  <Cube currency='SGD' rate='1.5494'/>
                  <Cube currency='THB' rate='37.527'/>
                  <Cube currency='ZAR' rate='17.4919'/></Cube>
              </Cube>
            </gesmes:Envelope>
        """.trimIndent()

        val mockResponse = mockResponseString.byteInputStream()

    }

}