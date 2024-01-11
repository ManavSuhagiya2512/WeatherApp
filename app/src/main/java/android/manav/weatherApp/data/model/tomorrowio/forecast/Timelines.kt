package android.p.weatherApp.data.model.tomorrowio.forecast

import kotlinx.serialization.Serializable

@Serializable
data class Timelines(
    val minutely: List<Minutely>,
    val hourly: List<Hourly>,
    val daily: List<Daily>
)