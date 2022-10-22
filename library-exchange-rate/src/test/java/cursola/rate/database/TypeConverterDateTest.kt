package cursola.rate.database

import org.junit.Before
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

internal class TypeConverterDateTest {

    private lateinit var converter: TypeConverterDate

    @Before
    fun prepare() {
        converter = TypeConverterDate()
    }

    @Test
    fun to_returnsMillis() {
        val result = converter.to(Date(100))
        assertEquals(100, result)
    }

    @Test
    fun from_returnsDate() {
        val result = converter.from(100)
        assertEquals(Date(100), result)
    }

}