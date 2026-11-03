package wiki.depasquale.currency.platform.process

import android.content.Intent

class SharedTextRequest(
    private val intent: Intent,
    private val copying: () -> ConvertedValueTarget
) : ConvertTextRequest {

    override fun text(): CharSequence? = try {
        intent.getCharSequenceExtra(Intent.EXTRA_TEXT) ?: ""
    } catch (_: RuntimeException) {
        null
    }

    override fun target(): ConvertedValueTarget = copying()

}
