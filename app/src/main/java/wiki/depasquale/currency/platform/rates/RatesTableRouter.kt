package wiki.depasquale.currency.platform.rates

import android.net.Uri

internal class RatesTableRouter(
    private val tables: Map<String, RatesTable>
) : RatesTable {

    override fun type(uri: Uri) = tables[path(uri)]?.type(uri)

    override fun columns(uri: Uri) = table(uri).columns(uri)

    override suspend fun rows(uri: Uri) = table(uri).rows(uri)

    private fun table(uri: Uri) = requireNotNull(tables[path(uri)]) { "Unsupported uri $uri" }

    private fun path(uri: Uri) = uri.pathSegments.joinToString("/")

}
