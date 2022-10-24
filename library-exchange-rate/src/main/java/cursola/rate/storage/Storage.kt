package cursola.rate.storage

interface Storage {

    operator fun set(name: String, value: String)
    operator fun get(name: String): String

}