package android.p.weatherApp.data.model.tomorrowio.realtime

import kotlinx.serialization.Serializable

/**
 * https://docs.tomorrow.io/reference/data-layers-core
 */
@Serializable
data class Values(
    val cloudBase: Double?,
    val cloudCeiling: Double?,
    val cloudCover: Int,
    val dewPoint: Double,
    val freezingRainIntensity: Int,
    val humidity: Int,
    val precipitationProbability: Int,
    val pressureSurfaceLevel: Double,
    val rainIntensity: Int,
    val sleetIntensity: Int,
    val snowIntensity: Int,
    val temperature: Double,
    val temperatureApparent: Double,
    val uvHealthConcern: Int,
    val uvIndex: Int,
    val visibility: Double,
    val weatherCode: Int,
    val windDirection: Double?,
    val windGust: Int,
    val windSpeed: Double
)