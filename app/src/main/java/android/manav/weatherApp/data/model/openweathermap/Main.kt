package android.p.weatherApp.data.model.openweathermap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Main(
    val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val temp_min: Double,
    val temp_max: Double,
    @SerialName("sea_level") val seaLevel: Long = 0,
    @SerialName("grnd_level") val grndLevel: Long = 0
)