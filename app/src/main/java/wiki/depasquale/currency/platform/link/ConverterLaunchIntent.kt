package wiki.depasquale.currency.platform.link

import android.content.Intent

fun interface ConverterLaunchIntent {

    fun create(pair: CurrencyPair?): Intent

}
