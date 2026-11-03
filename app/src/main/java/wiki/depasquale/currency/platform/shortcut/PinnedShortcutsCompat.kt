package wiki.depasquale.currency.platform.shortcut

import android.content.Context
import androidx.core.content.pm.ShortcutManagerCompat
import wiki.depasquale.currency.R
import wiki.depasquale.currency.platform.link.CurrencyPair

class PinnedShortcutsCompat(
    private val context: Context
) : PinnedShortcuts {

    override fun keepOnly(pairs: Set<CurrencyPair>) {
        val valid = pairs.map { it.shortcutId() }.toSet()
        val (kept, retired) = ShortcutManagerCompat.getShortcuts(context, ShortcutManagerCompat.FLAG_MATCH_PINNED)
            .filter { it.id.startsWith(PAIR_SHORTCUT_PREFIX) }
            .partition { it.id in valid }
        if (retired.isNotEmpty()) {
            val message = context.getString(R.string.shortcut_pair_removed)
            ShortcutManagerCompat.disableShortcuts(context, retired.map { it.id }, message)
        }
        if (kept.isNotEmpty()) ShortcutManagerCompat.enableShortcuts(context, kept)
    }

}
