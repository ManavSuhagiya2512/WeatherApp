/*
 * Copyright (c) 2023. Makoto Sakaguchi, Thinh Quach, Xingning Xu, Rumleen Rathor. All rights reserved.
 *
 * This source code or any portion thereof must not be reproduced or used in any manner whatsoever.
 */

package android.p.weatherApp.database

import android.p.weatherApp.database.dao.FavoriteDao
import android.p.weatherApp.database.model.Favorite
import android.p.weatherApp.database.model.FavoriteFts
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Favorite::class,
        FavoriteFts::class
    ],
    version = 1,
    exportSchema = false,
)
//@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}