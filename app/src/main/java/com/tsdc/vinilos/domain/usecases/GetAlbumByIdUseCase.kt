package com.tsdc.vinilos.domain.usecases

import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.repositories.AlbumRepository

class GetAlbumByIdUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke(id: Int): Album? {
        return repository.getAlbumById(id)
    }
}
