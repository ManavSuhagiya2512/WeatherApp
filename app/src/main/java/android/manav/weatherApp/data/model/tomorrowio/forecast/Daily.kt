package android.p.weatherApp.data.model.tomorrowio.forecast

import kotlinx.serialization.Serializable

@Serializable
data class Daily(
    val time: String,
    val values: Values
)