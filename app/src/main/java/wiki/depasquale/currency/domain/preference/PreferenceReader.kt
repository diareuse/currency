package wiki.depasquale.currency.domain.preference

interface PreferenceReader<Model> {

    fun read(): Model

}