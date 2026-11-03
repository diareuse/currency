package wiki.depasquale.currency.platform.process

import android.app.Activity
import android.content.Intent

class ProcessTextRequest(
    private val activity: Activity,
    private val copying: () -> ConvertedValueTarget
) : ConvertTextRequest {

    override fun text(): CharSequence? = try {
        activity.intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT) ?: ""
    } catch (_: RuntimeException) {
        null
    }

    override fun target(): ConvertedValueTarget {
        val readOnly = activity.intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)
        if (readOnly) return copying()
        return ConvertedValueReplacesSelection(activity)
    }

}
