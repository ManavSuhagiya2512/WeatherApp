/*
 * Copyright (c) 2023. Makoto Sakaguchi, Thinh Quach, Xingning Xu, Rumleen Rathor. All rights reserved.
 *
 * This source code or any portion thereof must not be reproduced or used in any manner whatsoever.
 */

package android.p.weatherApp.network.helper

import kotlinx.coroutines.CancellableContinuation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class JsonNetworkCallback<T : Any> private constructor(
    private val deserializer: KSerializer<T>,
    private val continuation: CancellableContinuation<T>
) : InMemoryTransformCallback() {
    @OptIn(ExperimentalSerializationApi::class)
    override fun onSucceeded(info: UrlResponseInfo, inputStream: InputStream) =
        continuation.resume(Json.decodeFromStream(deserializer, inputStream))

    override fun onFailed(request: UrlRequest, info: UrlResponseInfo?, error: CronetException) =
        continuation.resumeWithException(error)

    override fun onCanceled(request: UrlRequest, info: UrlResponseInfo?) {
        continuation.cancel(RequestCancelledException())
    }

    companion object {
        inline operator fun <reified T : Any> invoke(cont: CancellableContinuation<T>) =
            JsonNetworkCallback(serializer(), cont)
    }
}