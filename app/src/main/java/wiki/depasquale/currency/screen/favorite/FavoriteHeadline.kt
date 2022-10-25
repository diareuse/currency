package wiki.depasquale.currency.screen.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import wiki.depasquale.currency.R
import wiki.depasquale.currency.screen.style.CurrencyTheme

@Composable
fun FavoriteHeadline(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "My favorites", style = MaterialTheme.typography.titleLarge)

        Button(
            onClick = onAddClick,
            contentPadding = PaddingValues(8.dp),
            colors = ButtonDefaults.textButtonColors()
        ) {
            Icon(painter = painterResource(R.drawable.ic_edit), contentDescription = "edit")
            Spacer(Modifier.width(8.dp))
            Text("Edit")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CurrencyTheme {
        FavoriteHeadline(onAddClick = { /*TODO*/ })
    }
}