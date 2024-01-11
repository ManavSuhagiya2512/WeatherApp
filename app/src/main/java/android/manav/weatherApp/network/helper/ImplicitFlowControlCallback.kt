/*
 * Copyright (c) 2023. Makoto Sakaguchi, Thinh Quach, Xingning Xu, Rumleen Rathor. All rights reserved.
 *
 * This source code or any portion thereof must not be reproduced or used in any manner whatsoever.
 */

package android.p.weatherApp.network.helper

import android.p.weatherApp.network.util.CONTENT_LENGTH
import android.p.weatherApp.network.util.toArrayCapacity
import android.p.weatherApp.network.util.toByteBufferCapacity
import android.p.weatherApp.network.util.toContentLength
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.nio.ByteBuffer

abstract class ImplicitFlowControlCallback : UrlRequest.Callback() {
    final override fun onRedirectReceived(request: UrlRequest, info: UrlResponseInfo, newLocationUrl: String) =
        if (info.urlChain.contains(newLocationUrl)) request.cancel() else request.followRedirect()

    final override fun onResponseStarted(request: UrlRequest, info: UrlResponseInfo) {
        val contentLength = info.allHeaders[CONTENT_LENGTH].toContentLength()
        onResponseStarted(info, contentLength.toArrayCapacity())
        request.read(ByteBuffer.allocateDirect(contentLength.toByteBufferCapacity()))
    }

    final override fun onReadCompleted(request: UrlRequest, info: UrlResponseInfo, byteBuffer: ByteBuffer) {
        // The byte buffer we're getting in the callback hasn't been flipped for reading,
        // so flip it so we can read the content.
        byteBuffer.flip()

        onBodyChunkRead(info, byteBuffer)

        // Reset the buffer to prepare it for the next read
        byteBuffer.clear()

        // Continue reading the request
        request.read(byteBuffer)
    }

    /**
     * Invoked when the final set of headers, after all redirects, is received.
     * Will only be invoked once for each request.
     * It's guaranteed that the HTTP stack doesn't start reading the body until this method returns.
     *
     * @param info Response information.
     * @throws Exception if an error occurs while processing response start. [onFailed] will
     * be called with the thrown exception set as the cause of the [HttpException].
     */
    @Throws(Exception::class)
    protected open fun onResponseStarted(info: UrlResponseInfo, contentLength: Int) {
    }

    /**
     * Invoked whenever part of the response body has been read.
     * Only part of the buffer may be populated, even if the entire response body has not yet been consumed.
     * The buffer is ready for reading. Buffers are reused internally so
     * the implementing class shouldn't store the buffer or use it anywhere else than
     * in the implementation of this method.
     *
     * @param info Response information.
     * @param bodyChunk The buffer that contains the received data, flipped for reading.
     * @throws Exception if an error occurs while processing a read completion.
     * [onFailed] will be called with the thrown exception set as the cause of the [HttpException].
     */
    @Throws(Exception::class)
    protected abstract fun onBodyChunkRead(info: UrlResponseInfo, byteBuffer: ByteBuffer)
}