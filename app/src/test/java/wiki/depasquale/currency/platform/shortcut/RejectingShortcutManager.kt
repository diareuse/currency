package wiki.depasquale.currency.platform.shortcut

import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowShortcutManager

@Implements(ShortcutManager::class)
class RejectingShortcutManager : ShadowShortcutManager() {

    @Implementation
    override fun setDynamicShortcuts(shortcutInfoList: List<ShortcutInfo>): Boolean =
        throw IllegalStateException("Launcher rejected shortcuts")

}
