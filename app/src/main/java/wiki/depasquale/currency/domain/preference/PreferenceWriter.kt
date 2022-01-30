package wiki.depasquale.currency.domain.preference

interface PreferenceWriter<Model> {

    fun write(preference: Model)

}