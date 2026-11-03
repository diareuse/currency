package wiki.depasquale.currency.platform.process

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cursola.rate.view.ConvertTextRow
import cursola.rate.view.ConvertTextState
import wiki.depasquale.currency.R
import wiki.depasquale.currency.screen.favorite.FavoriteItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ConvertTextScreen(
    state: ConvertTextState,
    onSelect: (ConvertTextRow) -> Unit,
    onOpenApp: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        IgnoreTouchesThroughOverlays()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                ConvertTextState.Loading -> Loading()
                ConvertTextState.Unparseable -> Problem(R.string.convert_text_unparseable, onOpenApp)
                ConvertTextState.NoFavorites -> Problem(R.string.convert_text_no_favorites, onOpenApp)
                ConvertTextState.Unavailable -> Problem(R.string.convert_text_unavailable, onOpenApp)
                is ConvertTextState.Converted -> ConvertedRows(state.rows, onSelect)
            }
        }
    }
}

@Composable
private fun IgnoreTouchesThroughOverlays() {
    val view = LocalView.current
    SideEffect { view.rootView.filterTouchesWhenObscured = true }
}

@Composable
private fun Loading() {
    CircularProgressIndicator(Modifier.padding(16.dp))
    Text(stringResource(R.string.convert_text_loading), style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun ConvertedRows(rows: List<ConvertTextRow>, onSelect: (ConvertTextRow) -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in rows) {
            FavoriteItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(row) },
                name = row.name,
                value = row.value,
                currency = row.currency
            )
        }
    }
}

@Composable
private fun Problem(@StringRes message: Int, onOpenApp: () -> Unit) {
    Text(
        text = stringResource(message),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        style = MaterialTheme.typography.bodyLarge
    )
    TextButton(onClick = onOpenApp) {
        Text(stringResource(R.string.convert_text_open_app))
    }
}
