package android.p.weatherApp.data.repository.impl.tomorrowio

import android.p.weatherApp.R
import androidx.annotation.DrawableRes

private typealias UvIndex = Int
private typealias WeatherCode = Int

/**
 * The strength of UV radiation.
 */
fun UvIndex.toMessage() = when (this) {
    in 0..2 -> R.string.uv_low
    in 3..5 -> R.string.uv_moderate
    in 6..7 -> R.string.uv_high
    in 8..10 -> R.string.uv_very_high
    in 11..Int.MAX_VALUE -> R.string.uv_extreme
    else -> R.string.unknown
}

@DrawableRes
fun WeatherCode.toImage() = when (this) {
    1000 -> R.drawable.clear_10000
    1100 -> R.drawable.mostly_clear_11000
    1101 -> R.drawable.partly_cloudy_11010
    1102 -> R.drawable.mostly_cloudy_11020
    1001 -> R.drawable.cloudy_10010
    2000 -> R.drawable.fog_20000
    2100 -> R.drawable.fog_light_21000
    4000 -> R.drawable.drizzle_40000
    4001 -> R.drawable.rain_40010
    4200 -> R.drawable.rain_light_42000
    4201 -> R.drawable.rain_heavy_42010
    5000 -> R.drawable.snow_50000
    5001 -> R.drawable.flurries_50010
    5100 -> R.drawable.snow_light_51000
    5101 -> R.drawable.snow_heavy_51010
    6000 -> R.drawable.freezing_rain_drizzle_60000
    6001 -> R.drawable.freezing_rain_60010
    6200 -> R.drawable.freezing_rain_light_62000
    6201 -> R.drawable.freezing_rain_heavy_62010
    7000 -> R.drawable.ice_pellets_70000
    7101 -> R.drawable.ice_pellets_heavy_71010
    7102 -> R.drawable.ice_pellets_light_71020
    8000 -> R.drawable.tstorm_80000
    else -> 0
}