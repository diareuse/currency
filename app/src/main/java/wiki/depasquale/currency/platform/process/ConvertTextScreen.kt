package wiki.depasquale.currency.platform.process

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cursola.rate.view.ConvertTextRow
import cursola.rate.view.ConvertTextState
import wiki.depasquale.currency.R

@Composable
internal fun ConvertTextScreen(
    state: ConvertTextState,
    onSelect: (ConvertTextRow) -> Unit,
    onOpenApp: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
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
private fun Loading() {
    CircularProgressIndicator(Modifier.padding(16.dp))
    Text(stringResource(R.string.convert_text_loading), style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun ConvertedRows(rows: List<ConvertTextRow>, onSelect: (ConvertTextRow) -> Unit) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        for (row in rows) {
            ConvertedRow(row, onClick = { onSelect(row) })
        }
    }
}

@Composable
private fun ConvertedRow(row: ConvertTextRow, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(row.name, style = MaterialTheme.typography.bodyMedium)
        Text(row.value, style = MaterialTheme.typography.titleMedium)
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
