package wiki.depasquale.currency.platform.process

fun interface ConvertedValueTarget {

    fun accept(value: String)

}
