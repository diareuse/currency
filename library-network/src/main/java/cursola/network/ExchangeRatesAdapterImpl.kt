package cursola.network

import cursola.network.util.optionalTag
import cursola.network.util.skipEntry
import cursola.network.util.skipUntilTag
import cursola.network.util.tag
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date

internal class ExchangeRatesAdapterImpl(
    private val parser: XmlPullParser,
    formatter: SimpleDateFormat
) : ExchangeRatesAdapter {

    private val xml = XmlAdapter(formatter)

    override fun adapt(stream: InputStream): List<ExchangeRateResponse> {
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
        parser.setInput(stream, null)
        parser.nextTag()
        return xml.adapt(parser)
    }

    private class XmlAdapter(
        private val formatter: SimpleDateFormat
    ) {

        fun adapt(parser: XmlPullParser): List<ExchangeRateResponse> {
            val values = hashSetOf<ExchangeRateResponse>()
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

        private fun readValue(
            parser: XmlPullParser,
            values: HashSet<ExchangeRateResponse>,
            time: Date
        ) {
            val currency = parser.getAttributeValue(null, "currency")
            val rate = parser.getAttributeValue(null, "rate")
            values += ExchangeRateResponse(currency, rate, time)
            parser.skipEntry()
        }

    }

}
