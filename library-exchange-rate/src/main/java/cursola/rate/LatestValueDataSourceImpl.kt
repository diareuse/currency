package cursola.rate

import cursola.rate.storage.Storage

internal class LatestValueDataSourceImpl(
    private val storage: Storage
) : LatestValueDataSource {

    override var currency: String
        get() = storage["currency"]
        set(value) {
            storage["currency"] = value
        }

    override var value: String
        get() = storage["value"]
        set(value) {
            storage["value"] = value
        }

}