package com.tsdc.vinilos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.usecases.GetAlbumsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumViewModel(private val getAlbumsUseCase: GetAlbumsUseCase) : ViewModel() {
    
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun loadAlbums() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = getAlbumsUseCase()
                _albums.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al cargar álbumes"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
