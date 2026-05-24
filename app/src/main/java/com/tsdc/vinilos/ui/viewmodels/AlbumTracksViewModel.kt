package com.tsdc.vinilos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.Track
import com.tsdc.vinilos.domain.usecases.AddTrackToAlbumUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumByIdUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumTracksViewModel(
    private val getAlbumTracksUseCase: GetAlbumTracksUseCase,
    private val addTrackToAlbumUseCase: AddTrackToAlbumUseCase,
    private val getAlbumByIdUseCase: GetAlbumByIdUseCase
) : ViewModel() {

    private val _album = MutableStateFlow<Album?>(null)
    val album: StateFlow<Album?> = _album

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _trackAdded = MutableStateFlow(false)
    val trackAdded: StateFlow<Boolean> = _trackAdded

    fun load(albumId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _tracks.value = emptyList()
            try {
                _album.value = getAlbumByIdUseCase(albumId)
                _tracks.value = getAlbumTracksUseCase(albumId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar los tracks del álbum"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTracks(albumId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _tracks.value = getAlbumTracksUseCase(albumId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar los tracks del álbum"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh(albumId: Int) = load(albumId)

    fun addTrack(albumId: Int, name: String, duration: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _trackAdded.value = false
            try {
                val newTrack = addTrackToAlbumUseCase(albumId, name, duration)
                _tracks.value = _tracks.value + newTrack
                _trackAdded.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al agregar el track al álbum"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetTrackAdded() {
        _trackAdded.value = false
    }
}
