package wiki.depasquale.currency.screen.listing

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import wiki.depasquale.currency.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingSearchField(
    filter: String,
    onFilterChanged: (String) -> Unit
) {
    val style = LocalTextStyle.current
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = filter,
        onValueChange = onFilterChanged,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(stringResource(R.string.search_cta), style = style)
        },
        trailingIcon = {
            if (filter.isBlank())
                Icon(painterResource(R.drawable.ic_search), null)
            else
                IconButton(onClick = { onFilterChanged("") }) {
                    Icon(painterResource(R.drawable.ic_clear), null)
                }
        }
    )
}