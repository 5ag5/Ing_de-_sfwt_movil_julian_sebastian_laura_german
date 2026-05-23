package com.tsdc.vinilos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tsdc.vinilos.domain.usecases.AddTrackToAlbumUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumByIdUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumTracksUseCase

class AlbumTracksViewModelFactory(
    private val getAlbumTracksUseCase: GetAlbumTracksUseCase,
    private val addTrackToAlbumUseCase: AddTrackToAlbumUseCase,
    private val getAlbumByIdUseCase: GetAlbumByIdUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumTracksViewModel::class.java)) {
            return AlbumTracksViewModel(
                getAlbumTracksUseCase,
                addTrackToAlbumUseCase,
                getAlbumByIdUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
