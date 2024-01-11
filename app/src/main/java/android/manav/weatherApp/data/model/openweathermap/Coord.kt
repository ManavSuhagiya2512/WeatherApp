package android.p.weatherApp.data.model.openweathermap

import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lon: Double,
    val lat: Double
)