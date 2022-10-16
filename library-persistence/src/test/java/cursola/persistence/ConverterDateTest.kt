package cursola.persistence

import org.junit.Before
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class ConverterDateTest {

    private lateinit var converter: ConverterDate

    @Before
    fun prepare() {
        converter = ConverterDate()
    }

    @Test
    fun from_returnsInstance() {
        val expected = Date()
        assertEquals(expected, converter.from(expected.time))
    }

    @Test
    fun to_returnsCode() {
        val expected = Date()
        assertEquals(expected.time, converter.to(expected))
    }

}