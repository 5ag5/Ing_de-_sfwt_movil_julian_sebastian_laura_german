package com.tsdc.vinilos.domain.usecases

import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.domain.repositories.ArtistRepository

class GetArtistByIdUseCase(private val repository: ArtistRepository) {
    suspend operator fun invoke(id: Int): Artist = repository.getArtistById(id)
}
