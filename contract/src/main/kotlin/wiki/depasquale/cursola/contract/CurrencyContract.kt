package wiki.depasquale.cursola.contract

/**
 * Read-only exchange rates exposed by Cursola. Callers hold [PERMISSION_READ_RATES] and, from API 30,
 * declare `<queries><provider android:authorities="wiki.depasquale.cursola.rates" /></queries>`
 * so package visibility filtering lets them reach the provider.
 */
object CurrencyContract {

    const val AUTHORITY = "wiki.depasquale.cursola.rates"
    const val PERMISSION_READ_RATES = "wiki.depasquale.cursola.permission.READ_RATES"
    const val CONTENT_URI = "content://$AUTHORITY"
    private const val PATH_PREFIX = "$CONTENT_URI/"

    object Currencies {
        const val PATH = "currencies"
        const val CONTENT_URI = PATH_PREFIX + PATH
        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.wiki.depasquale.cursola.currency"

        const val CODE = "code"
        const val DISPLAY_NAME = "display_name"
        const val SYMBOL = "symbol"

        @JvmField
        val COLUMNS: List<String> = listOf(CODE, DISPLAY_NAME, SYMBOL)
    }

    object LatestRates {
        const val PATH = "rates/latest"
        const val CONTENT_URI = PATH_PREFIX + PATH
        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.wiki.depasquale.cursola.rate"

        const val PARAM_BASE = "base"
        const val DEFAULT_BASE = "EUR"

        const val CODE = Currencies.CODE
        const val RATE = "rate"
        const val TIMESTAMP_MS = "timestamp_ms"

        @JvmField
        val COLUMNS: List<String> = listOf(CODE, RATE, TIMESTAMP_MS)
    }

    object Conversions {
        const val PATH = "convert"
        const val CONTENT_URI = PATH_PREFIX + PATH
        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.wiki.depasquale.cursola.conversion"

        const val PARAM_AMOUNT = "amount"
        const val PARAM_FROM = "from"
        const val PARAM_TO = "to"

        const val FROM = PARAM_FROM
        const val TO = PARAM_TO
        const val AMOUNT = PARAM_AMOUNT
        const val RESULT = "result"
        const val RATE = LatestRates.RATE
        const val TIMESTAMP_MS = LatestRates.TIMESTAMP_MS

        @JvmField
        val COLUMNS: List<String> = listOf(FROM, TO, AMOUNT, RESULT, RATE, TIMESTAMP_MS)
    }

}
