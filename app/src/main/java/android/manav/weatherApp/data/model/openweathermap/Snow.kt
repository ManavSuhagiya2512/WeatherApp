package android.p.weatherApp.data.model.openweathermap

import kotlinx.serialization.Serializable

@Serializable
data class Snow(
    val `1h`: Double,
    val `3h`: Double
)