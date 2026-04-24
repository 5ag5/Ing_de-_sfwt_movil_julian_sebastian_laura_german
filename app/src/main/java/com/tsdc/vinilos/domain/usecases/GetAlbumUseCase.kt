package com.tsdc.vinilos.domain.usecases

import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.repositories.AlbumRepository

class GetAlbumsUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke(): List<Album> {
        return repository.getAlbums()
    }
}