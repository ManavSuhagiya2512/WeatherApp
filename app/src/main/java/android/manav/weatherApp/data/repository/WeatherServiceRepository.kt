package android.p.weatherApp.data.repository

import android.p.weatherApp.data.model.openweathermap.WeatherApp
import android.p.weatherApp.network.helper.JsonNetworkCallback
import android.icu.util.ULocale
import android.location.Location
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.chromium.net.CronetEngine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.DecimalFormat
import java.util.concurrent.ExecutorService

private const val WEATHER_SERVICE_ENDPOINT = "https://api.openweathermap.org/data/2.5/weather"

class WeatherServiceRepository(private val apiKey: String) : KoinComponent {
    private val networkEngine: CronetEngine by inject()
    private val executor: ExecutorService by inject()

    private val degreeFormat: DecimalFormat by lazy { DecimalFormat("###.#####") }

    suspend fun fetchCurrentWeather(location: Location, locale: ULocale) = fetchCurrentWeather(
        latitude = degreeFormat.format(location.latitude),
        longitude = degreeFormat.format(location.longitude),
        locale = locale
    )

    suspend fun fetchCurrentWeather(latitude: String, longitude: String, locale: ULocale): WeatherApp =
        withContext(Dispatchers.IO) {
            Log.d("WeatherServiceRepository", "Lat: $latitude, Lon: $longitude")

            suspendCancellableCoroutine { continuation ->
                val request = networkEngine.newUrlRequestBuilder(
                    "$WEATHER_SERVICE_ENDPOINT?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric&lang=${
                        ULocale.minimizeSubtags(
                            locale
                        ).toLanguageTag()
                    }",
                    JsonNetworkCallback(continuation),
                    executor
                ).addHeader(ACCEPT, APPLICATION_JSON)
                    .build()

                request.start()

                continuation.invokeOnCancellation { request.cancel() }
            }
        }

    suspend fun fetchCurrentWeather(city: String, locale: ULocale): WeatherApp = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            val request = networkEngine.newUrlRequestBuilder(
                "$WEATHER_SERVICE_ENDPOINT/?q=$city&appid=$apiKey&units=metric&lang=${
                    ULocale.minimizeSubtags(locale).toLanguageTag()
                }",
                JsonNetworkCallback(continuation),
                executor
            ).addHeader(ACCEPT, APPLICATION_JSON)
                .build()

            request.start()

            continuation.invokeOnCancellation { request.cancel() }
        }
    }

    companion object {
        const val ACCEPT = "Accept"
        const val APPLICATION_JSON = "application/json"
    }
}