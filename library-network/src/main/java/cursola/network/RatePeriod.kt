package cursola.network

import java.net.URL

enum class RatePeriod(
    val url: URL
) {

    Daily("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"),
    Last90Days("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml"),
    Historical("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml");

    constructor(url: String) : this(URL(url))

}