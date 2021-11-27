package org.billthefarmer.currency.domain.preference

interface PreferenceWriter<Model> {

    fun write(preference: Model)

}