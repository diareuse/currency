package wiki.depasquale.currency.platform.process

import android.app.Activity
import android.content.Intent

class ConvertedValueReplacesSelection(
    private val activity: Activity
) : ConvertedValueTarget {

    override fun accept(value: String) {
        activity.setResult(Activity.RESULT_OK, Intent().putExtra(Intent.EXTRA_PROCESS_TEXT, value))
    }

}
