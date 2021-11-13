package org.billthefarmer.currency.domain.rate

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.domain.adapter.ExchangeRatesAdapter
import org.billthefarmer.currency.domain.adapter.ExchangeRatesAdapterImplTest
import org.billthefarmer.currency.domain.network.NetworkService
import org.billthefarmer.currency.domain.network.NetworkServiceImplTest
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.net.URL

class ExchangeRatesNetworkTest : MockableTest() {

    private lateinit var rates: ExchangeRatesNetwork
    private lateinit var url: URL

    @Mock
    lateinit var network: NetworkService

    @Mock
    lateinit var adapter: ExchangeRatesAdapter

    @Before
    fun prepare() {
        url = URL("https://localhost")
        rates = ExchangeRatesNetwork(url, network, adapter)

        Mockito.`when`(network.get(url))
            .thenAnswer { NetworkServiceImplTest.mockResponse }
        Mockito.`when`(adapter.adapt(NetworkServiceImplTest.mockResponse))
            .thenAnswer { ExchangeRatesAdapterImplTest.singleResponse }
    }

    @Test
    fun `returns non empty values`() {
        val rates = rates.getCurrentRates()
        assertThat(rates).isNotEmpty()
    }

    @Test
    fun `returns values provided by network`() {
        val rates = rates.getCurrentRates()
        val expected = ExchangeRatesAdapterImplTest.singleResponse

        assertThat(rates).containsExactlyElementsIn(expected)
    }

}