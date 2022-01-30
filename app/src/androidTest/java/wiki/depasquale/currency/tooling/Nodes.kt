package wiki.depasquale.currency.tooling

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection

inline fun SemanticsNodeInteractionCollection.forEach(body: (SemanticsNodeInteraction) -> Unit) {
    repeat(fetchSemanticsNodes().size) {
        body(get(it))
    }
}

inline fun SemanticsNodeInteractionCollection.forEachIndexed(body: (Int, SemanticsNodeInteraction) -> Unit) {
    repeat(fetchSemanticsNodes().size) {
        body(it, get(it))
    }
}