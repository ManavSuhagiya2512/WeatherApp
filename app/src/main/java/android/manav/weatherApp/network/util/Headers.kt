/*
 * Copyright (c) 2023. Makoto Sakaguchi, Thinh Quach, Xingning Xu, Rumleen Rathor. All rights reserved.
 *
 * This source code or any portion thereof must not be reproduced or used in any manner whatsoever.
 */

package android.p.weatherApp.network.util

const val CONTENT_LENGTH = "Content-Length"

private const val BYTE_BUFFER_CAPACITY = 32 * 1024

private typealias HeaderValue = List<String>
private typealias ContentLength = Long

/**
 * Returns the numerical value of the Content-Length header, or `-1` if not set or invalid.
 */
fun HeaderValue?.toContentLength() = if (this?.size == 1) {
    try {
        this[0].toLong()
    } catch (e: NumberFormatException) {
        -1
    }
} else {
    -1
}

fun ContentLength.toArrayCapacity() = when {
    this > Int.MAX_VALUE - 8 -> Int.MAX_VALUE - 8
    this >= 0 -> this.toInt()
    else -> BYTE_BUFFER_CAPACITY
}

fun ContentLength.toByteBufferCapacity() = when {
    this > Int.MAX_VALUE -> Int.MAX_VALUE
    this >= 0 -> this.toInt()
    else -> BYTE_BUFFER_CAPACITY
}