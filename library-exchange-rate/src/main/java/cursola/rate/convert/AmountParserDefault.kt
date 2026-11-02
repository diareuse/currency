package cursola.rate.convert

import java.math.BigDecimal
import java.text.DecimalFormatSymbols
import java.util.Locale

class AmountParserDefault(
    private val currencies: CurrencyTokenResolver
) : AmountParser {

    override fun parse(text: CharSequence, locale: Locale): Money? {
        if (text.length > MAX_LENGTH) return null
        val normalized = text.toString()
            .replace(spaces, " ")
            .substringAfterLast(':')
            .trim()
            .trimStart { it in openers }
            .trimEnd { it in closers }
            .trim()
        val match = number.findAll(normalized).singleOrNull() ?: return null
        val token = currencyToken(normalized, match.range) ?: return null
        val currency = currencies.resolve(token, locale) ?: return null
        val amount = amount(match.value, locale) ?: return null
        return Money(amount, currency)
    }

    private fun currencyToken(text: String, numberRange: IntRange): String? {
        val prefix = text.substring(0, numberRange.first).trim()
        val suffix = text.substring(numberRange.last + 1).trim()
        return when {
            prefix.isNotEmpty() && suffix.isNotEmpty() -> null
            prefix.isNotEmpty() -> prefix
            suffix.isNotEmpty() -> suffix
            else -> null
        }
    }

    private fun amount(raw: String, locale: Locale): BigDecimal? {
        val decimal = decimalSeparator(raw, locale)
        val integer = if (decimal == null) raw else raw.substringBeforeLast(decimal)
        val fraction = if (decimal == null) "" else raw.substringAfterLast(decimal)
        if (!fraction.all(Char::isDigit)) return null
        val digits = groupedDigits(integer) ?: return null
        return BigDecimal(if (fraction.isEmpty()) digits else "$digits.$fraction")
    }

    private fun decimalSeparator(raw: String, locale: Locale): Char? {
        val candidates = raw.filter { it == '.' || it == ',' }
        val kinds = candidates.toSet()
        return when {
            kinds.size > 1 -> candidates.last()
            candidates.length != 1 -> null
            else -> singleSeparatorRole(raw, candidates.single(), locale)
        }
    }

    private fun singleSeparatorRole(raw: String, separator: Char, locale: Locale): Char? {
        val groupedOtherwise = raw.any { it in groupingOnly }
        val digitsAfter = raw.substringAfter(separator).length
        val localDecimal = DecimalFormatSymbols.getInstance(locale).decimalSeparator
        val isDecimal = groupedOtherwise || digitsAfter != GROUP_SIZE || localDecimal == separator
        return separator.takeIf { isDecimal }
    }

    private fun groupedDigits(integer: String): String? {
        if (integer.contains('.') && integer.contains(',')) return null
        val groups = integer.split(groupSeparators)
        if (groups.any { it.isEmpty() || !it.all(Char::isDigit) }) return null
        if (groups.size == 1) return groups.single()
        return groups.joinToString("").takeIf { isWesternGrouping(groups) || isIndianGrouping(groups) }
    }

    private fun isWesternGrouping(groups: List<String>) =
        groups.first().length <= GROUP_SIZE && groups.drop(1).all { it.length == GROUP_SIZE }

    private fun isIndianGrouping(groups: List<String>) =
        groups.size > 2 &&
            groups.first().length <= LAKH_GROUP_SIZE &&
            groups.subList(1, groups.lastIndex).all { it.length == LAKH_GROUP_SIZE } &&
            groups.last().length == GROUP_SIZE

    private companion object {
        const val GROUP_SIZE = 3
        const val LAKH_GROUP_SIZE = 2
        const val MAX_LENGTH = 256
        val openers = setOf('(', '[', '{', '"', '\'', '«', '“', '‘', '„')
        val closers = setOf('.', ',', ';', '!', '?', ')', ']', '}', '"', '\'', '»', '”', '’')
        val spaces = Regex("[\\p{Zs}\\s]+")
        val number = Regex("\\d(?:[\\d.,'’ ]*\\d)?")
        val groupingOnly = setOf(' ', '\'', '’')
        val groupSeparators = Regex("[ '’.,]")
    }

}
