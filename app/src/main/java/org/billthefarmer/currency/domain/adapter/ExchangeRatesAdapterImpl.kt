package org.billthefarmer.currency.domain.adapter

import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.model.PersistedRate
import org.billthefarmer.currency.domain.tooling.optionalTag
import org.billthefarmer.currency.domain.tooling.skipEntry
import org.billthefarmer.currency.domain.tooling.skipUntilTag
import org.billthefarmer.currency.domain.tooling.tag
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class ExchangeRatesAdapterImpl(
    private val parser: XmlPullParser,
    formatter: SimpleDateFormat
) : ExchangeRatesAdapter {

    private val xml = XmlAdapter(formatter)

    override fun adapt(rate: PersistedRate): ExchangeRate {
        return ExchangeRate(rate.currency, rate.rate, rate.time)
    }

    override fun adapt(stream: InputStream): List<ExchangeRate> {
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
        parser.setInput(stream, null)
        parser.nextTag()
        return xml.adapt(parser)
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
