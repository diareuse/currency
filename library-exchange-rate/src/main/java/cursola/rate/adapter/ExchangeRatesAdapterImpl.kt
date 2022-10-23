package cursola.rate.adapter

import cursola.rate.ExchangeRate
import cursola.rate.util.optionalTag
import cursola.rate.util.skipEntry
import cursola.rate.util.skipUntilTag
import cursola.rate.util.tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

class ExchangeRatesAdapterImpl(
    private val parser: XmlPullParser
) : ExchangeRatesAdapter {

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val xml = XmlAdapter(formatter)

    override suspend fun adapt(stream: InputStream): List<ExchangeRate> {
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
        parser.setInput(stream, null)
        parser.nextTag()
        return withContext(Dispatchers.IO) {
            xml.adapt(parser)
        }
    }

    private class XmlAdapter(
        private val formatter: SimpleDateFormat
    ) {

        fun adapt(parser: XmlPullParser): List<ExchangeRate> {
            val values = hashSetOf<ExchangeRate>()
            parser.tag("Envelope") {
                skipUntilTag("Cube") {
                    val time = parser.getAttributeValue(null, "time")
                    optionalTag("Cube") {
                        readValue(parser, values, adaptTime(time))
                    }
                }
            }
            return values.toList()
        }

        private fun adaptTime(time: String): Date {
            return requireNotNull(formatter.parse(time))
        }

        private fun readValue(parser: XmlPullParser, values: HashSet<ExchangeRate>, time: Date) {
            val currency = parser.getAttributeValue(null, "currency").toCurrency()
            val rate = parser.getAttributeValue(null, "rate").toDouble()
            values += ExchangeRate(currency, rate, time)
            parser.skipEntry()
        }

    }

}

private fun String.toCurrency(): Currency {
    return Currency.getInstance(uppercase())
}
