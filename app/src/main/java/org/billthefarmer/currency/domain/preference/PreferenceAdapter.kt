package org.billthefarmer.currency.domain.preference

interface PreferenceAdapter {

    fun fromPreference(input: Any?): Any?
    fun toPreference(input: Any?): Any?

}