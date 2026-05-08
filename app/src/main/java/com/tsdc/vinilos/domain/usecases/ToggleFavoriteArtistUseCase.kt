package com.tsdc.vinilos.domain.usecases

import com.tsdc.vinilos.domain.repositories.ArtistRepository

class ToggleFavoriteArtistUseCase(private val repository: ArtistRepository) {
    suspend operator fun invoke(artistId: Int) = repository.toggleFavorite(artistId)
}
