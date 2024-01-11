package android.p.weatherApp.favorites

import android.p.weatherApp.data.repository.FavoriteRepository
import android.p.weatherApp.database.model.Favorite
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class FavoriteListViewModel : ViewModel(), KoinComponent {
    private val favoriteRepo: FavoriteRepository by inject()

    val favorites = favoriteRepo.loadFavorites().cachedIn(viewModelScope)

    fun updateFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepo.updateFavorite(favorite)
        }
    }

    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepo.deleteFavorite(favorite.id)
        }
    }
}