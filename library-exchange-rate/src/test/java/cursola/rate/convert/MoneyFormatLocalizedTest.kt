package cursola.rate.convert

import java.math.BigDecimal
import java.util.Currency
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class MoneyFormatLocalizedTest {

    private val icuDigits = CurrencyFractionDigits {
        com.ibm.icu.util.Currency.getInstance(it.currencyCode).defaultFractionDigits
    }
    private val format = MoneyFormatLocalized(icuDigits)

    private fun format(amount: String, code: String) =
        format.format(Money(BigDecimal(amount), Currency.getInstance(code)), Locale.US)

    @Test
    fun icu_reports_two_fraction_digits_for_gold_and_none_for_yen_and_krona() {
        assertEquals(2, icuDigits.of(Currency.getInstance("XAU")))
        assertEquals(0, icuDigits.of(Currency.getInstance("JPY")))
        assertEquals(0, icuDigits.of(Currency.getInstance("ISK")))
    }

    @Test
    fun small_gold_amount_keeps_four_significant_digits() {
        assertEquals("XAU0.00384", format("0.00384", "XAU"))
    }

    @Test
    fun large_gold_amount_keeps_its_minor_units() {
        assertEquals("XAU3,950.62", format("3950.61696", "XAU"))
        assertEquals("XAU3,950.62", format("3950.6249", "XAU"))
    }

    @Test
    fun large_drawing_rights_amount_keeps_its_minor_units() {
        assertEquals("XDR12,345.67", format("12345.67", "XDR"))
    }

    @Test
    fun sub_cent_amount_shows_significant_fraction() {
        assertEquals("$0.001", format("0.001", "USD"))
    }

    @Test
    fun ordinary_amount_is_padded_to_currency_fraction_digits() {
        assertEquals("$9.60", format("9.6", "USD"))
    }

    @Test
    fun inexact_ordinary_amount_is_rounded_to_currency_fraction_digits() {
        assertEquals("$9.60", format("9.6049", "USD"))
        assertEquals("$0.92", format("0.92345", "USD"))
        assertEquals("$11.08", format("11.08148", "USD"))
    }

    @Test
    fun currency_without_minor_units_rounds_half_up_to_whole_units() {
        assertEquals("¥1,440", format("1440.48", "JPY"))
        assertEquals("¥3", format("2.5", "JPY"))
        assertEquals("ISK1,235", format("1234.5", "ISK"))
    }

    @Test
    fun zero_is_plain_zero_at_currency_fraction_digits() {
        assertEquals("XAU0.00", format("0.0000", "XAU"))
    }

    @Test
    fun currency_reporting_no_fraction_digits_is_treated_as_two() {
        val format = MoneyFormatLocalized { -1 }

        assertEquals("XAU0.00384", format.format(Money(BigDecimal("0.00384"), Currency.getInstance("XAU")), Locale.US))
        assertEquals("XAU3,950.62", format.format(Money(BigDecimal("3950.61696"), Currency.getInstance("XAU")), Locale.US))
    }

}
