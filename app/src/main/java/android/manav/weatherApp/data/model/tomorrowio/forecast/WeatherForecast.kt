package android.p.weatherApp.data.model.tomorrowio.forecast

import android.p.weatherApp.data.model.tomorrowio.Location
import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecast(
    val timelines: Timelines,
    val location: Location
)