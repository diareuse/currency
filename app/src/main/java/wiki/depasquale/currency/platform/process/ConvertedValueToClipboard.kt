package wiki.depasquale.currency.platform.process

import android.content.ClipData
import android.content.ClipboardManager

class ConvertedValueToClipboard(
    private val clipboard: ClipboardManager,
    private val label: CharSequence
) : ConvertedValueTarget {

    override fun accept(value: String) {
        clipboard.setPrimaryClip(ClipData.newPlainText(label, value))
    }

}
