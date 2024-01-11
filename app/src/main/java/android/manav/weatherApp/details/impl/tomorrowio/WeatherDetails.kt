package android.p.weatherApp.details.impl.tomorrowio

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class WeatherDetails(
    val cloudBase: String? = "No clouds",
    val cloudCeiling: String? = "No clouds",
    val cloudCover: String,
    val dewPoint: String,
    val freezingRainIntensity: String,
    val humidity: String,
    val precipitationProbability: String,
    val pressureSurfaceLevel: String,
    val rainIntensity: String,
    val sleetIntensity: String,
    val snowIntensity: String,
    val temperature: String,
    val temperatureApparent: String,
    val uvIndex: Int,
    @StringRes val uvHealthConcern: Int,
    val visibility: String,
    @DrawableRes val weatherCode: Int,
    val windDirection: String? = "No wind",
    val windGust: String,
    val windSpeed: String
)