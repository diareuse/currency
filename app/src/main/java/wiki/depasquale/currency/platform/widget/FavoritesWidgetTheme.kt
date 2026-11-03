package wiki.depasquale.currency.platform.widget

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.glance.GlanceTheme
import androidx.glance.material3.ColorProviders
import wiki.depasquale.currency.screen.style.currencyColorsDark
import wiki.depasquale.currency.screen.style.currencyColorsLight

@Composable
internal fun FavoritesWidgetTheme(content: @Composable () -> Unit) {
    val colors = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> GlanceTheme.colors
        else -> ColorProviders(light = currencyColorsLight, dark = currencyColorsDark)
    }
    GlanceTheme(colors = colors, content = content)
}
