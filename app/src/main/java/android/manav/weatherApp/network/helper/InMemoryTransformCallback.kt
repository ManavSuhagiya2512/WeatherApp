/*
 * Copyright (c) 2023. Makoto Sakaguchi, Thinh Quach, Xingning Xu, Rumleen Rathor. All rights reserved.
 *
 * This source code or any portion thereof must not be reproduced or used in any manner whatsoever.
 */

package android.p.weatherApp.network.helper

import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.WritableByteChannel
import kotlin.concurrent.thread

abstract class InMemoryTransformCallback : ImplicitFlowControlCallback() {
    private lateinit var responseBodyStream: ByteArrayOutputStream
    private lateinit var responseBodyChannel: WritableByteChannel

    final override fun onResponseStarted(info: UrlResponseInfo, contentLength: Int) {
        responseBodyStream = ByteArrayOutputStream(contentLength)
        responseBodyChannel = Channels.newChannel(responseBodyStream)
    }

    final override fun onBodyChunkRead(info: UrlResponseInfo, byteBuffer: ByteBuffer) {
        responseBodyChannel.write(byteBuffer)
    }

    override fun onSucceeded(request: UrlRequest, info: UrlResponseInfo) {
        // Create a PipedInputStream; automatically close it at the end of the lambda
        PipedInputStream().use { inputStream ->
            // Run the given lambda in a new thread
            thread {
                // Create a PipedOutputStream and connect it to the PipedInputStream;
                // automatically close it at the end of the lambda
                PipedOutputStream(inputStream).use { outputStream ->
                    responseBodyStream.writeTo(outputStream)
                }
            }

            onSucceeded(info, inputStream)
        }
    }

    /**
     * Transforms (deserializes) the plain full body into a user-defined object.
     *
     * It is assumed that the implementing classes handle edge cases
     * (such as empty and malformed bodies) appropriately.
     * The HTTP stack doesn't inspect the objects and passes them (or any exceptions)
     * along to the issuer of the request.
     */
    protected abstract fun onSucceeded(info: UrlResponseInfo, inputStream: InputStream)
}