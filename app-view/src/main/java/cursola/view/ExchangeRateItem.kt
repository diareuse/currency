package cursola.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ExchangeRateItem(
    flag: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp, 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        flag()
        text()
    }
}

@Composable
fun ExchangeRateItem(
    icon: @Composable () -> Unit,
    flag: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = Color.Transparent,
    shape: Shape = RectangleShape,
    elevation: Dp = 0.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .width(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Row(
            modifier = modifier
                .fillMaxWidth()
                .shadow(elevation)
                .background(background, shape)
                .clip(shape)
                .padding(12.dp, 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            flag()
            text()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview1() {
    ExchangeRateItem(
        flag = {
            Image(painterResource(android.R.drawable.arrow_up_float), null)
        },
        text = {
            Column {
                Text("US Dollar")
                CurrencyValue("$123.45", "$")
            }
        },
        modifier = Modifier
            .padding(16.dp)
            .clickable {}
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    ExchangeRateItem(
        icon = {
            Image(painterResource(android.R.drawable.ic_delete), null)
        },
        flag = {
            Image(painterResource(android.R.drawable.arrow_up_float), null)
        },
        text = {
            Column {
                Text("US Dollar")
                CurrencyValue("$123.45", "$")
            }
        },
        modifier = Modifier
            .padding(16.dp)
            .clickable {},
        background = Color.LightGray,
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp
    )
}