package android.p.weatherApp.di

import android.p.weatherApp.data.repository.WeatherServiceRepository
import android.p.weatherApp.database.WeatherDatabase
import android.p.weatherApp.details.DetailsViewModel
import android.p.weatherApp.format.time.DateTimeFormatManager
import android.icu.number.NumberFormatter
import android.icu.text.DateFormat
import android.icu.util.MeasureUnit
import android.os.LocaleList
import androidx.paging.PagingConfig
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import org.chromium.net.CronetEngine
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.includes
import org.koin.core.qualifier.named
import org.koin.dsl.lazyModule
import java.util.concurrent.Executors

internal const val GENERAL_FORMATTER = "General"
internal const val INTENSITY_FORMATTER = "Intensity"

private const val DEFAULT_PAGING_SIZE = 5

@OptIn(KoinExperimentalAPI::class)
internal val appModule = lazyModule {
    includes(
        databaseModule,
        dateTimeFormatManagerModule,
        networkModule,
        numberFormatterModule,
        pagingConfigModel,
        openWeatherApiModule
    )

    viewModelOf(::DetailsViewModel)
}

@OptIn(KoinExperimentalAPI::class)
private val databaseModule = lazyModule {
    single {
        Room.databaseBuilder(
            androidContext(),
            WeatherDatabase::class.java,
            "local-db"
        ).build()
    }

    single { get<WeatherDatabase>().favoriteDao() }
}

@OptIn(KoinExperimentalAPI::class)
val dateTimeFormatManagerModule = lazyModule {
    single {
        DateTimeFormatManager(
            defaultDispatcher = Dispatchers.Default,
            dateSkeleton = DateFormat.YEAR_NUM_MONTH_DAY,
            locale = LocaleList.getAdjustedDefault()[0],
            context = androidContext()
        )
    }
}

@OptIn(KoinExperimentalAPI::class)
private val pagingConfigModel = lazyModule {
    single { PagingConfig(pageSize = DEFAULT_PAGING_SIZE) }
}

@OptIn(KoinExperimentalAPI::class)
private val openWeatherApiModule = lazyModule {
    single {
        WeatherServiceRepository(apiKey = getProperty("weather-api-key"))
    }
}

/*
@OptIn(KoinExperimentalAPI::class)
private val tomorrowIoApiModule = lazyModule {
    single {
        TomorrowIoApi(apiKey = getProperty("weather-api-key"))
    }
}
*/

@OptIn(KoinExperimentalAPI::class)
private val networkModule = lazyModule {
    single {
        CronetEngine.Builder(androidContext())
            .enableHttp2(true)
            .enableQuic(true)
            .enableBrotli(true)
            .build()
    }

    single { Executors.newCachedThreadPool() }
}

@OptIn(KoinExperimentalAPI::class)
private val numberFormatterModule = lazyModule {
    single(named(GENERAL_FORMATTER)) { NumberFormatter.with() }
    single(named(INTENSITY_FORMATTER)) { NumberFormatter.with().unit(MeasureUnit.MILLIMETER).perUnit(MeasureUnit.HOUR) }
}