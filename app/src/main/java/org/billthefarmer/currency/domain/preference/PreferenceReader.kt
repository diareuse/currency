package org.billthefarmer.currency.domain.preference

interface PreferenceReader<Model> {

    fun read(): Model

}