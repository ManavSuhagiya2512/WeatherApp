package android.p.weatherApp.data.model.openweathermap

import kotlinx.serialization.Serializable

@Serializable
data class Clouds(
    val all: Int
)