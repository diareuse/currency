package cursola.rate

sealed class ExchangeRateError : IllegalStateException() {

    class NotFoundException(
        override val cause: Throwable,
        override val message: String
    ) : ExchangeRateError()

}