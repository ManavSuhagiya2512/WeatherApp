package android.p.weatherApp.data.model.openweathermap

import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    val speed: Double,
    val deg: Double,
    val gust: Double = Double.NaN
)