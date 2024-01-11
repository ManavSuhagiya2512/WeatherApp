package android.p.weatherApp.data.model.tomorrowio

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val lat: Double,
    val lon: Double,
    val name: String?,
    val type: String?
)