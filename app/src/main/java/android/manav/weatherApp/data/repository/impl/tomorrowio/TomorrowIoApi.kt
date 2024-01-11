package android.p.weatherApp.data.repository.impl.tomorrowio

import android.p.weatherApp.data.model.tomorrowio.realtime.RealtimeWeather
import android.p.weatherApp.network.helper.JsonNetworkCallback
import android.location.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.chromium.net.CronetEngine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.DecimalFormat
import java.util.concurrent.ExecutorService

// The Realtime Weather GET endpoint
private const val REALTIME_URL = "https://api.tomorrow.io/v4/weather/realtime"

class TomorrowIoApi(private val apiKey: String) : KoinComponent {
    private val networkEngine: CronetEngine by inject()
    private val executor: ExecutorService by inject()

    private val degreeFormat: DecimalFormat by lazy { DecimalFormat("###.#####") }

    suspend fun fetchCurrentWeather(location: Location) = fetchCurrentWeather(
        buildString {
            append(degreeFormat.format(location.latitude))
            append(',')
            append(degreeFormat.format(location.longitude))
        }
    )

    suspend fun fetchCurrentWeather(location: String): RealtimeWeather = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            val request = networkEngine.newUrlRequestBuilder(
                "$REALTIME_URL/?location=$location",
                JsonNetworkCallback(continuation),
                executor
            ).addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(API_KEY, apiKey)
                .build()

            request.start()

            continuation.invokeOnCancellation { request.cancel() }
        }
    }

    companion object {
        const val CONTENT_TYPE = "Accept"
        const val APPLICATION_JSON = "application/json"
        private const val API_KEY = "apikey"
    }
}