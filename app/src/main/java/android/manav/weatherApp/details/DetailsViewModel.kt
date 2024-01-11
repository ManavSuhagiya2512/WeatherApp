package android.p.weatherApp.details

import android.p.weatherApp.data.model.openweathermap.WeatherApp
import android.p.weatherApp.data.model.openweathermap.util.WeatherImage
import android.p.weatherApp.data.model.openweathermap.util.toBackgroundImage
import android.p.weatherApp.data.repository.FavoriteRepository
import android.p.weatherApp.data.repository.WeatherServiceRepository
import android.p.weatherApp.database.model.Favorite
import android.p.weatherApp.di.GENERAL_FORMATTER
import android.p.weatherApp.di.INTENSITY_FORMATTER
import android.p.weatherApp.format.time.DateTimeFormatManager
import android.icu.number.LocalizedNumberFormatter
import android.icu.number.UnlocalizedNumberFormatter
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.icu.util.ULocale
import android.location.Location
import android.os.LocaleList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale

internal class DetailsViewModel : ViewModel(), KoinComponent {
    private val weatherRepo: WeatherServiceRepository by inject()
    private val favoriteRepo: FavoriteRepository by inject()
    private val dateTimeFormatManager: DateTimeFormatManager by inject()

    private val mutex: Mutex by lazy { Mutex() }

    private val dateFormatter get() = dateTimeFormatManager.dateFormatter
    private val timeFormatter get() = dateTimeFormatManager.timeFormatter
    private val dateTimeFormatter get() = dateTimeFormatManager.dateTimeFormatter
    private val dayNameFormatter get() = dateTimeFormatManager.dayNameFormatter

    private val numberFormatter: LocalizedNumberFormatter by lazy {
        get<UnlocalizedNumberFormatter>(named(GENERAL_FORMATTER)).locale(locale)
    }
    private val intensityFormatter: LocalizedNumberFormatter by lazy {
        get<UnlocalizedNumberFormatter>(named(INTENSITY_FORMATTER)).locale(locale)
    }

    private val _lastUpdate = MutableLiveData<String>()
    val lastUpdate: LiveData<String> = _lastUpdate

    private val _weatherDetails = MutableLiveData<WeatherDetails>()
    val weatherDetails: LiveData<WeatherDetails> = _weatherDetails

    private val _weatherImage = MutableLiveData<WeatherImage>()
    val weatherImage: LiveData<WeatherImage> = _weatherImage

    private var locale = ULocale.forLocale(LocaleList.getAdjustedDefault()[0])
    private var weatherData: WeatherApp? = null

    fun setLocale(newLocale: Locale) {
        viewModelScope.launch {
            mutex.withLock { locale = ULocale.forLocale(newLocale) }
            dateTimeFormatManager.onLocaleChanged(newLocale)
        }
    }

    fun fetchWeatherData(location: Location) {
        viewModelScope.launch {
            weatherRepo.fetchCurrentWeather(location, locale).let {
                weatherData = it
                _lastUpdate.postValue(dateTimeFormatter.format(LocalDateTime.now()))
                _weatherDetails.postValue(it.toWeatherDetails())
            }
        }
    }

    fun fetchWeatherData(city: String) {
        viewModelScope.launch {
            weatherRepo.fetchCurrentWeather(city, locale).let {
                weatherData = it
                _lastUpdate.postValue(dateTimeFormatter.format(LocalDateTime.now()))
                _weatherDetails.postValue(it.toWeatherDetails())
            }
        }
    }

    fun saveLocation() {
        viewModelScope.launch {
            weatherData?.coord?.run {
                favoriteRepo.updateFavorite(
                    Favorite(latitude = lat, longitude = lon)
                )
            }
        }
    }

    private suspend fun WeatherApp.toWeatherDetails() = withContext(Dispatchers.Default) {
        weather.firstOrNull()?.run { _weatherImage.postValue(id.toBackgroundImage()) }

        WeatherDetails(
            cityName = name,
            humidity = numberFormatter.format(Measure(main.humidity, MeasureUnit.PERCENT)).toString(),
            temperature = numberFormatter.format(Measure(main.temp, MeasureUnit.CELSIUS)).toString(),
            windSpeed = numberFormatter.format(Measure(wind.speed, MeasureUnit.METER_PER_SECOND)).toString(),
            sunRise = timeFormatter.format(LocalDateTime.ofEpochSecond(sys.sunrise, 0, ZoneOffset.UTC)),
            sunSet = timeFormatter.format(LocalDateTime.ofEpochSecond(sys.sunset, 0, ZoneOffset.UTC)),
            seaLevel = numberFormatter.format(Measure(main.pressure, MeasureUnit.HECTOPASCAL)).toString(),
            minTemp = numberFormatter.format(Measure(main.temp_min, MeasureUnit.CELSIUS)).toString(),
            maxTemp = numberFormatter.format(Measure(main.temp_max, MeasureUnit.CELSIUS)).toString(),
            weather = weather.firstOrNull()?.description ?: "unknown",
            condition = weather.firstOrNull()?.main ?: "unknown",
            day = dayNameFormatter.format(LocalDate.now()),
            date = dateFormatter.format(LocalDate.now()),
            feelsLike = numberFormatter.format(Measure(main.feelsLike, MeasureUnit.CELSIUS)).toString()

        )
    }

    /*private suspend fun Values.toWeatherDetails() = withContext(Dispatchers.Default) {
        WeatherDetails(
            cloudBase = cloudBase?.let {
                numberFormatter.format(Measure(it, MeasureUnit.KILOMETER)).toString()
            },
            cloudCeiling = cloudCeiling?.let {
                numberFormatter.format(Measure(it, MeasureUnit.KILOMETER)).toString()
            },
            cloudCover = numberFormatter.format(Measure(cloudCover, MeasureUnit.PERCENT)).toString(),
            dewPoint = numberFormatter.format(Measure(dewPoint, MeasureUnit.CELSIUS)).toString(),
            freezingRainIntensity = intensityFormatter.format(freezingRainIntensity).toString(),
            humidity = numberFormatter.format(Measure(humidity, MeasureUnit.PERCENT)).toString(),
            precipitationProbability = numberFormatter.format(
                Measure(precipitationProbability, MeasureUnit.PERCENT)
            ).toString(),
            pressureSurfaceLevel = numberFormatter.format(
                Measure(pressureSurfaceLevel, MeasureUnit.HECTOPASCAL)
            ).toString(),
            rainIntensity = intensityFormatter.format(rainIntensity).toString(),
            sleetIntensity = intensityFormatter.format(sleetIntensity).toString(),
            snowIntensity = intensityFormatter.format(snowIntensity).toString(),
            temperature = numberFormatter.format(Measure(temperature, MeasureUnit.CELSIUS)).toString(),
            temperatureApparent = numberFormatter.format(
                Measure(temperatureApparent, MeasureUnit.CELSIUS)
            ).toString(),
            uvIndex = uvIndex,
            uvHealthConcern = uvHealthConcern.toMessage(),
            visibility = numberFormatter.format(Measure(visibility, MeasureUnit.KILOMETER)).toString(),
            weatherCode = weatherCode.toImage(),
            windDirection = windDirection?.let {
                numberFormatter.format(Measure(it, MeasureUnit.DEGREE)).toString()
            },
            windGust = numberFormatter.format(Measure(windGust, MeasureUnit.METER_PER_SECOND)).toString(),
            windSpeed = numberFormatter.format(Measure(windSpeed, MeasureUnit.METER_PER_SECOND)).toString()
        )
    }*/
}