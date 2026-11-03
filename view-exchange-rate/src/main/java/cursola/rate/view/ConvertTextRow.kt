package cursola.rate.view

import java.util.Currency

data class ConvertTextRow(
    val currency: Currency,
    val name: String,
    val value: String
)
