package wiki.depasquale.currency.platform.link

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class ConverterLaunchIntentActivity(
    private val context: Context,
    private val target: Class<out Activity>
) : ConverterLaunchIntent {

    override fun create(pair: CurrencyPair?): Intent {
        if (pair == null) return Intent(context, target).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return Intent(Intent.ACTION_VIEW, pair.toConvertLink().toUri())
            .setClass(context, target)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

}
