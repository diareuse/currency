package cursola.rate.convert

import java.util.Currency
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AmountParserDefaultTest {

    private val parser = AmountParserDefault(
        CurrencyTokenResolverFirstMatch(
            listOf(CurrencyTokenResolverIso(), CurrencyTokenResolverSymbol())
        )
    )

    private fun parse(text: String, locale: Locale = Locale.US) = parser.parse(text, locale)

    @Test
    fun parses_iso_code_after_number() {
        assertEquals(money("12", USD), parse("12 USD"))
    }

    @Test
    fun parses_iso_code_before_number() {
        assertEquals(money("12", USD), parse("USD 12"))
    }

    @Test
    fun parses_iso_code_in_any_case_without_space() {
        assertEquals(money("30", EUR), parse("30eur"))
        assertEquals(money("30", EUR), parse("Eur30"))
    }

    @Test
    fun parses_euro_pound_and_yen_symbols_on_either_side() {
        assertEquals(money("30", EUR), parse("€30"))
        assertEquals(money("30", EUR), parse("30 €"))
        assertEquals(money("5", GBP), parse("£5"))
        assertEquals(money("500", JPY), parse("¥500"))
    }

    @Test
    fun parses_dollar_symbol_as_us_dollar_in_us_locale() {
        assertEquals(money("12", USD), parse("$12"))
    }

    @Test
    fun parses_dollar_symbol_as_local_dollar_in_dollar_locale() {
        assertEquals(money("12", Currency.getInstance("CAD")), parse("$12", Locale.CANADA))
    }

    @Test
    fun parses_dollar_symbol_as_us_dollar_in_non_dollar_locale() {
        assertEquals(money("12", USD), parse("$12", Locale.GERMANY))
    }

    @Test
    fun parses_country_prefixed_dollar_symbols() {
        assertEquals(money("7", Currency.getInstance("AUD")), parse("A$7"))
        assertEquals(money("7", USD), parse("US$ 7", Locale.CANADA))
    }

    @Test
    fun parses_yen_symbol_as_yuan_in_chinese_locale() {
        assertEquals(money("8", Currency.getInstance("CNY")), parse("¥8", Locale.CHINA))
    }

    @Test
    fun parses_comma_grouping_with_dot_decimal() {
        assertEquals(money("1234.5", EUR), parse("1,234.5 eur"))
    }

    @Test
    fun parses_dot_grouping_with_comma_decimal() {
        assertEquals(money("1234.5", EUR), parse("1.234,5 eur"))
    }

    @Test
    fun parses_repeated_grouping_without_decimal() {
        assertEquals(money("1234567", USD), parse("1,234,567 USD"))
        assertEquals(money("1234567", EUR), parse("1.234.567 EUR", Locale.US))
    }

    @Test
    fun parses_single_separator_not_followed_by_three_digits_as_decimal() {
        assertEquals(money("12.5", EUR), parse("12,5 EUR"))
        assertEquals(money("12.50", USD), parse("12.50 USD", Locale.GERMANY))
    }

    @Test
    fun resolves_ambiguous_three_digit_group_by_locale_decimal_separator() {
        assertEquals(money("1234", USD), parse("1,234 USD", Locale.US))
        assertEquals(money("1.234", USD), parse("1,234 USD", Locale.GERMANY))
    }

    @Test
    fun parses_no_break_and_thin_space_grouping() {
        assertEquals(money("1234.5", EUR), parse("1\u00A0234,5\u00A0€", Locale.FRANCE))
        assertEquals(money("1234567", EUR), parse("1\u202F234\u2009567 EUR", Locale.FRANCE))
    }

    @Test
    fun parses_other_unicode_space_separators() {
        assertEquals(money("1234", EUR), parse("1\u2006234\u205FEUR", Locale.FRANCE))
    }

    @Test
    fun returns_null_for_mixed_dot_and_comma_grouping() {
        assertNull(parse("1.234,567.8 USD"))
    }

    @Test
    fun returns_null_for_negative_amount() {
        assertNull(parse("-12 USD"))
    }

    @Test
    fun parses_apostrophe_grouping() {
        assertEquals(money("1234.5", Currency.getInstance("CHF")), parse("CHF 1'234.50"))
    }

    @Test
    fun ignores_surrounding_whitespace() {
        assertEquals(money("12", USD), parse("  12 USD \n"))
    }

    @Test
    fun returns_null_without_currency() {
        assertNull(parse("12"))
    }

    @Test
    fun returns_null_without_number() {
        assertNull(parse("USD"))
    }

    @Test
    fun returns_null_for_unknown_currency_token() {
        assertNull(parse("12 ABC"))
        assertNull(parse("12 dollars please"))
    }

    @Test
    fun returns_null_for_currency_on_both_sides() {
        assertNull(parse("$12 USD"))
    }

    @Test
    fun returns_null_for_malformed_grouping() {
        assertNull(parse("1,23,4 USD"))
        assertNull(parse("12 34 USD"))
    }

    @Test
    fun returns_null_for_several_numbers() {
        assertNull(parse("12 USD 13"))
    }

    @Test
    fun ignores_trailing_sentence_punctuation() {
        assertEquals(money("30", EUR), parse("€30."))
        assertEquals(money("12", USD), parse("12 USD."))
        assertEquals(money("12", USD), parse("12 USD!"))
    }

    @Test
    fun ignores_leading_label() {
        assertEquals(money("30", EUR), parse("Total: 30 EUR"))
    }

    @Test
    fun ignores_surrounding_brackets_and_quotes() {
        assertEquals(money("12", USD), parse("(12 USD)"))
        assertEquals(money("12", USD), parse("\"12 USD\""))
    }

    @Test
    fun keeps_leading_decimal_point_digits() {
        assertEquals(money("0.5", USD), parse("0.5 USD"))
    }

    @Test
    fun parses_indian_lakh_grouping() {
        assertEquals(money("123456", Currency.getInstance("INR")), parse("1,23,456 INR"))
        assertEquals(money("12345678", Currency.getInstance("INR")), parse("₹1,23,45,678"))
    }

    @Test
    fun returns_null_for_overly_long_text() {
        assertNull(parse("12 USD " + " ".repeat(300)))
        assertNull(parse("1".repeat(300) + " USD"))
    }

    @Test
    fun returns_null_for_label_with_text_after_currency() {
        assertNull(parse("Total: 30 EUR due"))
    }

    @Test
    fun returns_null_for_empty_text() {
        assertNull(parse(""))
    }

}
