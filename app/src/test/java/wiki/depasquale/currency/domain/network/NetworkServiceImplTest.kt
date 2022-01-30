package wiki.depasquale.currency.domain.network

import com.google.common.truth.Truth.assertThat
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test
import java.net.URL

class NetworkServiceImplTest {

    private lateinit var service: NetworkServiceImpl

    @Before
    fun prepare() {
        service = NetworkServiceImpl()
    }

    @Test
    fun `returns input stream`() {
        val url = URL("https://example.org")
        service.get(url).use {
            assertThat(it).isNotNull()
        }
    }

    companion object {

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