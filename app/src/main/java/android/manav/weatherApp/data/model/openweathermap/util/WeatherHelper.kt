package android.p.weatherApp.data.model.openweathermap.util

import android.p.weatherApp.R

private typealias WeatherId = Int
private typealias WeatherCode = Int

/**
 * The strength of UV radiation.
 */
fun WeatherId.toBackgroundImage() = when (this) {
    200, 201, 202, 210, 211, 212, 221, 230, 231, 232 -> WeatherImage(R.drawable.rain_background, R.raw.rain)
    300, 301, 302, 310, 311, 312, 313, 314, 321 -> WeatherImage(R.drawable.cloud_background, R.raw.cloud)
    500, 501, 502, 503, 504, 511, 520, 521, 522, 531 -> WeatherImage(R.drawable.rain_background, R.raw.rain)
    600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> WeatherImage(R.drawable.snow_background, R.raw.snow)
    701, 711, 721, 731, 741, 751, 761, 762, 771, 781 -> WeatherImage(R.drawable.cloud_background, R.raw.cloud)
    800 -> WeatherImage(R.drawable.sunny_background, R.raw.sun)
    in 801..804 -> WeatherImage(R.drawable.cloud_background, R.raw.cloud)
    else -> WeatherImage(R.drawable.cloud_background, R.raw.cloud)
}