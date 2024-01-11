package android.p.weatherApp.format.time

import android.content.Context
import android.icu.text.DateFormat
import android.icu.text.DateTimePatternGenerator
import android.os.LocaleList
import android.provider.Settings
import androidx.core.text.util.LocalePreferences.HourCycle
import androidx.core.text.util.LocalePreferences.getHourCycle
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val HOUR_FORMAT_12 = "12"
private const val HOUR_FORMAT_24 = "24"

private const val HOUR11_MINUTE = "Km"
private const val HOUR12_MINUTE = "hm"

private const val HOUR23_MINUTE = DateFormat.HOUR24_MINUTE
private const val HOUR24_MINUTE = "km"

fun DateTimePatternGenerator.getDateFormatter(
    skeleton: String = DateFormat.YEAR_NUM_MONTH_DAY,
    locale: Locale
): DateTimeFormatter = DateTimeFormatter.ofPattern(getBestPattern(skeleton), locale)

/**
 * Creates a [DateTimeFormatter] using the skeleton with hour and minute,
 * with the local preferred hour format (12 or 24).
 *
 * @param skeleton a pattern containing only the variable fields.
 */
fun DateTimePatternGenerator.getTimeFormatter(skeleton: String, locale: Locale): DateTimeFormatter =
    DateTimeFormatter.ofPattern(getBestPattern(skeleton), locale)

fun DateTimePatternGenerator.getDateTimeFormatter(
    dateSkeleton: String = DateFormat.YEAR_NUM_MONTH_DAY,
    timeSkeleton: String = DateFormat.HOUR_MINUTE,
    locale: Locale = LocaleList.getAdjustedDefault()[0]
): DateTimeFormatter = DateTimeFormatter.ofPattern(getBestDateTimePattern(dateSkeleton, timeSkeleton), locale)

/**
 * Return the best time pattern, hour and minute, with the local preferred hour format (12 or 24), based on the hour cycle setting.
 *
 * @param context The context to use for the content resolver.
 * @param locale The Locale to get the hour cycle.
 *
 * @return Best pattern matching the input skeleton.
 */
fun DateTimePatternGenerator.getBestTimePattern(
    context: Context,
    locale: Locale = LocaleList.getAdjustedDefault()[0]
): String = getBestPattern(getTimeSkeleton(context, locale))

fun DateTimePatternGenerator.getBestDateTimePattern(
    dateSkeleton: String = DateFormat.YEAR_NUM_MONTH_DAY,
    timeSkeleton: String
): String = getBestPattern("$dateSkeleton$timeSkeleton")

fun getTimeSkeleton(
    context: Context,
    locale: Locale = LocaleList.getAdjustedDefault()[0]
) = getTimeSkeleton(
    hourSystem = Settings.System.getString(context.contentResolver, Settings.System.TIME_12_24),
    locale = locale
)

fun getTimeSkeleton(hourSystem: String, locale: Locale) = when (hourSystem) {
    HOUR_FORMAT_12 -> {
        when (getHourCycle(locale, true)) {
            HourCycle.H11, HourCycle.H23 -> HOUR11_MINUTE
            HourCycle.H12, HourCycle.H24 -> HOUR12_MINUTE
            else -> DateFormat.HOUR_MINUTE
        }
    }

    HOUR_FORMAT_24 -> {
        when (getHourCycle(locale, true)) {
            HourCycle.H11, HourCycle.H23 -> HOUR23_MINUTE
            HourCycle.H12, HourCycle.H24 -> HOUR24_MINUTE
            else -> DateFormat.HOUR_MINUTE
        }
    }

    else -> DateFormat.HOUR_MINUTE
}