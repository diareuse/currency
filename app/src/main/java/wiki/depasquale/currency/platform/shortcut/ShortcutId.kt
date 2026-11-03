package wiki.depasquale.currency.platform.shortcut

import wiki.depasquale.currency.platform.link.CurrencyPair

internal const val PAIR_SHORTCUT_PREFIX = "convert-"

internal fun CurrencyPair.shortcutId() = "$PAIR_SHORTCUT_PREFIX${from.currencyCode}-${to.currencyCode}"
