/*
 * Copyright (c) 2023. Makoto Sakaguchi, Thinh Quach, Xingning Xu, Rumleen Rathor. All rights reserved.
 *
 * This source code or any portion thereof must not be reproduced or used in any manner whatsoever.
 */

package android.p.weatherApp.format.time

import android.content.Context
import android.icu.text.DateFormat
import android.icu.text.DateTimePatternGenerator
import android.provider.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference

class DateTimeFormatManager(
    private val defaultDispatcher: CoroutineDispatcher,
    private val dateSkeleton: String,
    private var locale: Locale,
    context: Context
) : KoinComponent {
    private val mutex: Mutex by lazy { Mutex() }

    private val timeSkeleton = AtomicReference<String>()

    private val _generator = AtomicReference<DateTimePatternGenerator>()
    private val generator
        get() = _generator.get() ?: _generator.updateAndGet {
            DateTimePatternGenerator.getInstance(locale)
        }

    private val _dateFormatter = AtomicReference<DateTimeFormatter>()
    val dateFormatter: DateTimeFormatter
        get() = _dateFormatter.get() ?: _dateFormatter.updateAndGet {
            generator.getDateFormatter(DateFormat.YEAR_MONTH_DAY, locale)
        }

    private val _timeFormatter = AtomicReference<DateTimeFormatter>()
    val timeFormatter: DateTimeFormatter
        get() = _timeFormatter.get() ?: _timeFormatter.updateAndGet {
            generator.getTimeFormatter(getTimeSkeleton(), locale)
        }

    private val _dateTimeFormatter = AtomicReference<DateTimeFormatter>()
    val dateTimeFormatter: DateTimeFormatter
        get() = _dateTimeFormatter.get() ?: _dateTimeFormatter.updateAndGet {
            generator.getDateTimeFormatter(
                dateSkeleton = dateSkeleton,
                timeSkeleton = getTimeSkeleton(),
                locale = locale
            )
        }

    private val _dayNameFormatter = AtomicReference<DateTimeFormatter>()
    val dayNameFormatter: DateTimeFormatter
        get() = _dayNameFormatter.get() ?: _dayNameFormatter.updateAndGet {
            DateTimeFormatter.ofPattern(generator.getBestPattern(DateFormat.WEEKDAY), locale)
        }

    @Volatile
    private var hourSystem = Settings.System.getString(context.contentResolver, Settings.System.TIME_12_24).orEmpty()

    suspend fun onLocaleChanged(locale: Locale) = withContext(defaultDispatcher) {
        mutex.withLock { this@DateTimeFormatManager.locale = locale }

        _generator.set(null)
        _dateTimeFormatter.set(null)
        timeSkeleton.set(null)
    }

    suspend fun onHourSystemChanged(hourSystem: String) = withContext(defaultDispatcher) {
        mutex.withLock { this@DateTimeFormatManager.hourSystem = hourSystem }

        _dateTimeFormatter.set(null)
        timeSkeleton.set(null)
    }

    private fun getTimeSkeleton() = timeSkeleton.get() ?: timeSkeleton.updateAndGet {
        getTimeSkeleton(hourSystem, locale)
    }
}