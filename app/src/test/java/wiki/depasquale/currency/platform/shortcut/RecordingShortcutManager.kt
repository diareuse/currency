package wiki.depasquale.currency.platform.shortcut

import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowShortcutManager

@Implements(ShortcutManager::class)
class RecordingShortcutManager : ShadowShortcutManager() {

    var dynamicPublishes = 0
        private set
    val disabled = mutableListOf<Pair<String, String>>()
    val enabled = mutableListOf<String>()
    val events = mutableListOf<String>()

    @Implementation
    override fun setDynamicShortcuts(shortcutInfoList: List<ShortcutInfo>): Boolean {
        dynamicPublishes++
        events += "publish " + shortcutInfoList.map { it.id }.sorted().joinToString()
        return super.setDynamicShortcuts(shortcutInfoList)
    }

    @Implementation
    override fun disableShortcuts(shortcutIds: List<String>, disabledMessage: CharSequence) {
        disabled += shortcutIds.map { it to disabledMessage.toString() }
        events += "disable " + shortcutIds.sorted().joinToString()
        super.disableShortcuts(shortcutIds, disabledMessage)
    }

    @Implementation
    override fun enableShortcuts(shortcutIds: List<String>) {
        enabled += shortcutIds
        events += "enable " + shortcutIds.sorted().joinToString()
        super.enableShortcuts(shortcutIds)
    }

}
