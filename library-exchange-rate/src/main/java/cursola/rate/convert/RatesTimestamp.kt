package cursola.rate.convert

import java.util.Date

fun interface RatesTimestamp {

    suspend fun get(): Date?

}
