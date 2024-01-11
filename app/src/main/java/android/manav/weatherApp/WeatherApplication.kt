package android.p.weatherApp

import android.p.weatherApp.di.appModule
import android.app.Application
import com.google.android.material.color.DynamicColors
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.lazyModules

class WeatherApplication : Application() {
    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WeatherApplication)
            androidFileProperties()
            lazyModules(appModule)
        }

        // Apply dynamic colors to all activities in the app
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}