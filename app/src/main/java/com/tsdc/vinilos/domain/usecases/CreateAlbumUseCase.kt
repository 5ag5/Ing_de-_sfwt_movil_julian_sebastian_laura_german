package com.tsdc.vinilos.domain.usecases

import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.NewAlbum
import com.tsdc.vinilos.domain.repositories.AlbumRepository

class CreateAlbumUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke(newAlbum: NewAlbum): Album = repository.createAlbum(newAlbum)
}
