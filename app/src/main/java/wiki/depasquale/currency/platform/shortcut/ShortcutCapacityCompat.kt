package wiki.depasquale.currency.platform.shortcut

import android.content.Context
import androidx.core.content.pm.ShortcutManagerCompat

class ShortcutCapacityCompat(
    private val context: Context,
    private val staticShortcuts: Int
) : ShortcutCapacity {

    override fun dynamicSlots(): Int =
        (ShortcutManagerCompat.getMaxShortcutCountPerActivity(context) - staticShortcuts).coerceAtLeast(0)

}
