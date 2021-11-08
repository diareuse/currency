package org.billthefarmer.currency.composition.tooling

import kotlin.random.Random

fun nextString(maxSize: Int = 20) =
    String(Random.nextBytes(Random.nextInt(0, maxSize)))

fun nextStringNot(excludedValue: String, maxSize: Int = 20): String {
    var string = nextString(maxSize = maxSize)
    while (string == excludedValue) {
        string = nextString(maxSize = maxSize)
    }
    return string
}