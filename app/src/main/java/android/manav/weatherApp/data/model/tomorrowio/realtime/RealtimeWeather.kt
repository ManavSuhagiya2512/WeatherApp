package android.p.weatherApp.data.model.tomorrowio.realtime

import android.p.weatherApp.data.model.tomorrowio.Location
import kotlinx.serialization.Serializable

@Serializable
data class RealtimeWeather(
    val data: Data,
    val location: Location
)
