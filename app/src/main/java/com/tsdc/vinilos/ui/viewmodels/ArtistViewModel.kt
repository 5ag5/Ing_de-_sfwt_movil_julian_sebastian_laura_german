package com.tsdc.vinilos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.domain.usecases.GetArtistsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistViewModel(private val getArtistsUseCase: GetArtistsUseCase) : ViewModel() {

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> = _artists

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadArtists() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _artists.value = getArtistsUseCase()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al cargar artistas"
            } finally {
                _isLoading.value = false
            }
        }
    }
}