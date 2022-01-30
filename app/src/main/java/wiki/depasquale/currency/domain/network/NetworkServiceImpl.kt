package wiki.depasquale.currency.domain.network

import java.io.InputStream
import java.net.URL

class NetworkServiceImpl : NetworkService {

    override fun get(url: URL): InputStream {
        return url.openStream()
    }

}