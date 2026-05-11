package com.tsdc.vinilos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.domain.usecases.GetArtistByIdUseCase
import com.tsdc.vinilos.domain.usecases.IsFavoriteArtistUseCase
import com.tsdc.vinilos.domain.usecases.ToggleFavoriteArtistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistDetailViewModel(
    private val getArtistByIdUseCase: GetArtistByIdUseCase,
    private val toggleFavoriteArtistUseCase: ToggleFavoriteArtistUseCase,
    private val isFavoriteArtistUseCase: IsFavoriteArtistUseCase
) : ViewModel() {

    private val _artist = MutableStateFlow<Artist?>(null)
    val artist: StateFlow<Artist?> = _artist

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun loadArtist(artistId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _artist.value = getArtistByIdUseCase(artistId)
                _isFavorite.value = isFavoriteArtistUseCase(artistId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al cargar el artista"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(artistId: Int) {
        viewModelScope.launch {
            try {
                toggleFavoriteArtistUseCase(artistId)
                _isFavorite.value = isFavoriteArtistUseCase(artistId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al actualizar favoritos"
            }
        }
    }
}
