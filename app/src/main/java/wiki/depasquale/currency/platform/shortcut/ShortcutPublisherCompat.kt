package wiki.depasquale.currency.platform.shortcut

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import wiki.depasquale.currency.R
import wiki.depasquale.currency.platform.link.CurrencyPair
import wiki.depasquale.currency.platform.link.toConvertLink

class ShortcutPublisherCompat(
    private val context: Context,
    private val target: Class<out Activity>
) : ShortcutPublisher {

    override fun publish(pairs: List<CurrencyPair>) {
        val shortcuts = pairs.mapIndexed(::toShortcut)
        val current = ShortcutManagerCompat.getDynamicShortcuts(context)
        if (shortcuts.signatures() == current.signatures()) return
        ShortcutManagerCompat.setDynamicShortcuts(context, shortcuts)
    }

    private fun toShortcut(rank: Int, pair: CurrencyPair): ShortcutInfoCompat {
        val intent = Intent(Intent.ACTION_VIEW, pair.toConvertLink().toUri())
            .setClass(context, target)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val label = context.getString(R.string.shortcut_pair_label, pair.from.currencyCode, pair.to.currencyCode)
        return ShortcutInfoCompat.Builder(context, pair.shortcutId())
            .setShortLabel(label)
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_shortcut_convert))
            .setIntent(intent)
            .setRank(rank)
            .build()
    }

    private fun List<ShortcutInfoCompat>.signatures() =
        map { Signature(it.id, it.rank, it.shortLabel.toString(), it.intent.toUri(0)) }.toSet()

    private data class Signature(val id: String, val rank: Int, val label: String, val intent: String)

}
