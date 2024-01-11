package android.p.weatherApp.data.model.tomorrowio.realtime

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val time: String,
    val values: Values
)
