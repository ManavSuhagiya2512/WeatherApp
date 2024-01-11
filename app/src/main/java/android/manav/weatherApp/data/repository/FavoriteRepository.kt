package android.p.weatherApp.data.repository

import android.p.weatherApp.database.dao.FavoriteDao
import android.p.weatherApp.database.model.Favorite
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FavoriteRepository : KoinComponent {
    private val favoriteDao: FavoriteDao by inject()
    private val pagingConfig: PagingConfig by inject()

    fun loadFavorites() = Pager(config = pagingConfig) { favoriteDao.loadFavorites() }.flow

    fun searchFavorite(text: CharSequence) = Pager(config = pagingConfig) {
        favoriteDao.searchFavorite("*${text}*")
    }.flow

    suspend fun updateFavorite(favorite: Favorite) = withContext(Dispatchers.IO) {
        favoriteDao.upsertFavorite(favorite)
    }

    suspend fun deleteFavorite(id: Int) = withContext(Dispatchers.IO) {
        favoriteDao.deleteById(id)
    }
}