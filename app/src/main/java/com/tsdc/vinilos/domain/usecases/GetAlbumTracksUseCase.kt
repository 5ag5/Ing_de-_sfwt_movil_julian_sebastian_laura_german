package com.tsdc.vinilos.domain.usecases

import com.tsdc.vinilos.domain.models.Track
import com.tsdc.vinilos.domain.repositories.AlbumRepository

class GetAlbumTracksUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke(albumId: Int): List<Track> =
        repository.getAlbumTracks(albumId)
}
