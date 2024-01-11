package android.p.weatherApp.database.dao

import android.p.weatherApp.database.model.Favorite
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface FavoriteDao {
    @Upsert
    suspend fun upsertFavorite(vararg favorites: Favorite)

    @Query("SELECT * FROM Favorite")
    fun loadFavorites(): PagingSource<Int, Favorite>

    @Query("SELECT * FROM Favorite\nJOIN FavoriteFts ON Favorite.id = FavoriteFts.id\nWHERE FavoriteFts.name MATCH :name")
    fun searchFavorite(name: String): PagingSource<Int, Favorite>

    @Query("DELETE FROM Favorite WHERE id = :id")
    suspend fun deleteById(id: Int)
}