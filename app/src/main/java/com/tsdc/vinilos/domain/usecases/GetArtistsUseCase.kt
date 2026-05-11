package com.tsdc.vinilos.domain.usecases


import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.domain.repositories.ArtistRepository

class GetArtistsUseCase(private val repository: ArtistRepository) {
    suspend operator fun invoke(): List<Artist>{
        return repository.getArtists()
    }
}