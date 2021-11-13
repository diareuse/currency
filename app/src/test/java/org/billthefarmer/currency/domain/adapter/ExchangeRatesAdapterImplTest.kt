package org.billthefarmer.currency.domain.adapter

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.model.PersistedRate
import org.billthefarmer.currency.domain.network.NetworkServiceImplTest
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Before
import org.junit.Test
import org.xmlpull.v1.XmlPullParserFactory
import java.text.SimpleDateFormat
import java.util.*

class ExchangeRatesAdapterImplTest : MockableTest() {

    private lateinit var adapter: ExchangeRatesAdapterImpl

    @Before
    fun prepare() {
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        adapter = ExchangeRatesAdapterImpl(parser, formatter)
    }

    @Test
    fun `returns parsed object from stream`() {
        val result = adapter.adapt(NetworkServiceImplTest.mockResponse)
        val expected = singleResponse

        assertThat(result).containsNoDuplicates()
        assertThat(result).containsExactlyElementsIn(expected)
    }

    @Test
    fun `returns translated object from persisted`() {
        val date = Date()
        val persisted = PersistedRate(Currency.getInstance("USD"), 0.0, date)
        val result = adapter.adapt(persisted)
        val expected = ExchangeRate(Currency.getInstance("USD"), 0.0, date)

        assertThat(result).isEqualTo(expected)
    }

    companion object {

        val date = Date(1636671600000)
        val singleResponse = listOf(
            ExchangeRate(Currency.getInstance("USD"), 1.1448, date),
            ExchangeRate(Currency.getInstance("JPY"), 130.50, date),
            ExchangeRate(Currency.getInstance("BGN"), 1.9558, date),
            ExchangeRate(Currency.getInstance("CZK"), 25.238, date),
            ExchangeRate(Currency.getInstance("DKK"), 7.4370, date),
            ExchangeRate(Currency.getInstance("GBP"), 0.85505, date),
            ExchangeRate(Currency.getInstance("HUF"), 366.15, date),
            ExchangeRate(Currency.getInstance("PLN"), 4.6428, date),
            ExchangeRate(Currency.getInstance("RON"), 4.9488, date),
            ExchangeRate(Currency.getInstance("SEK"), 10.0085, date),
            ExchangeRate(Currency.getInstance("CHF"), 1.0568, date),
            ExchangeRate(Currency.getInstance("ISK"), 150.40, date),
            ExchangeRate(Currency.getInstance("NOK"), 9.9508, date),
            ExchangeRate(Currency.getInstance("HRK"), 7.5123, date),
            ExchangeRate(Currency.getInstance("RUB"), 82.6649, date),
            ExchangeRate(Currency.getInstance("TRY"), 11.3987, date),
            ExchangeRate(Currency.getInstance("AUD"), 1.5690, date),
            ExchangeRate(Currency.getInstance("BRL"), 6.1902, date),
            ExchangeRate(Currency.getInstance("CAD"), 1.4416, date),
            ExchangeRate(Currency.getInstance("CNY"), 7.3047, date),
            ExchangeRate(Currency.getInstance("HKD"), 8.9206, date),
            ExchangeRate(Currency.getInstance("IDR"), 16239.91, date),
            ExchangeRate(Currency.getInstance("ILS"), 3.5600, date),
            ExchangeRate(Currency.getInstance("INR"), 85.1930, date),
            ExchangeRate(Currency.getInstance("KRW"), 1349.10, date),
            ExchangeRate(Currency.getInstance("MXN"), 23.6472, date),
            ExchangeRate(Currency.getInstance("MYR"), 4.7692, date),
            ExchangeRate(Currency.getInstance("NZD"), 1.6293, date),
            ExchangeRate(Currency.getInstance("PHP"), 56.995, date),
            ExchangeRate(Currency.getInstance("SGD"), 1.5494, date),
            ExchangeRate(Currency.getInstance("THB"), 37.527, date),
            ExchangeRate(Currency.getInstance("ZAR"), 17.4919, date),
        )

    }

}