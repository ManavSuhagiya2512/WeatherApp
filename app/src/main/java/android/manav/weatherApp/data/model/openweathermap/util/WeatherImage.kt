package android.p.weatherApp.data.model.openweathermap.util

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class WeatherImage(
    @DrawableRes val background: Int,
    @RawRes val animation: Int
)
