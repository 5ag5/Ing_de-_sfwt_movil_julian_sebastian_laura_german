package com.tsdc.vinilos.domain.usecases

import com.tsdc.vinilos.domain.repositories.ArtistRepository

class IsFavoriteArtistUseCase(private val repository: ArtistRepository) {
    suspend operator fun invoke(artistId: Int): Boolean = repository.isFavorite(artistId)
}
