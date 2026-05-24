package com.tsdc.vinilos.domain.usecases

import com.tsdc.vinilos.domain.models.Track
import com.tsdc.vinilos.domain.repositories.AlbumRepository

class AddTrackToAlbumUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke(albumId: Int, name: String, duration: String): Track =
        repository.addTrackToAlbum(albumId, name, duration)
}
