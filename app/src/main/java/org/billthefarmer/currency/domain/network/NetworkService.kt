package org.billthefarmer.currency.domain.network

import androidx.annotation.WorkerThread
import java.io.InputStream
import java.net.URL

interface NetworkService {

    @WorkerThread
    fun get(url: URL): InputStream

}