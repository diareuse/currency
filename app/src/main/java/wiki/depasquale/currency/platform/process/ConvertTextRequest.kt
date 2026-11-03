package wiki.depasquale.currency.platform.process

interface ConvertTextRequest {

    fun text(): CharSequence?

    fun target(): ConvertedValueTarget

}
