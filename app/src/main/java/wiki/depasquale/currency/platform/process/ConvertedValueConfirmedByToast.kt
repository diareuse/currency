package wiki.depasquale.currency.platform.process

import android.content.Context
import android.widget.Toast

class ConvertedValueConfirmedByToast(
    private val origin: ConvertedValueTarget,
    private val context: Context,
    private val message: CharSequence
) : ConvertedValueTarget {

    override fun accept(value: String) {
        origin.accept(value)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}
