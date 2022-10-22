package cursola.rate.database

import org.junit.Before
import org.junit.Test
import java.util.Currency
import kotlin.test.assertEquals

internal class TypeConverterCurrencyTest {

    private lateinit var converter: TypeConverterCurrency

    @Before
    fun prepare() {
        converter = TypeConverterCurrency()
    }

    @Test
    fun to_returnsCode() {
        val result = converter.to(Currency.getInstance("USD"))
        assertEquals("USD", result)
    }

    @Test
    fun from_returnsCurrency() {
        val result = converter.from("USD")
        assertEquals(Currency.getInstance("USD"), result)
    }

}