package android.p.weatherApp.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String? = "Favorite $id",
    val latitude: Double,
    val longitude: Double
)