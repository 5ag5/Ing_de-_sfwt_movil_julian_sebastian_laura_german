package com.tsdc.vinilos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.usecases.GetAlbumByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumDetailViewModel(
    private val getAlbumByIdUseCase: GetAlbumByIdUseCase
) : ViewModel() {

    private val _album = MutableStateFlow<Album?>(null)
    val album: StateFlow<Album?> = _album

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadAlbum(albumId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = getAlbumByIdUseCase(albumId)
                if (result == null) {
                    _error.value = "No se encontró el álbum"
                } else {
                    _album.value = result
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al cargar el álbum"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
