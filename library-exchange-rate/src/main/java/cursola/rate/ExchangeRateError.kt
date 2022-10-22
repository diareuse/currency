package cursola.rate

sealed class ExchangeRateError(
    override val message: String?
) : IllegalStateException() {

    class NotFoundException(
        message: String,
        override val cause: Throwable? = null
    ) : ExchangeRateError(message)

    class NoSourceAvailable : ExchangeRateError("Cannot find any sources")

}