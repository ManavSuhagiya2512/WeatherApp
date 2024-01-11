package android.p.weatherApp.data.model.openweathermap

import kotlinx.serialization.Serializable

@Serializable
data class WeatherApp(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val rain: Rain? = null,
    val snow: Snow? = null,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    @Deprecated("Built-in geocoder functionality has been deprecated.")
    val id: Int,
    @Deprecated("Built-in geocoder functionality has been deprecated.")
    val name: String,
    @Deprecated("Internal parameter")
    val cod: Int
)