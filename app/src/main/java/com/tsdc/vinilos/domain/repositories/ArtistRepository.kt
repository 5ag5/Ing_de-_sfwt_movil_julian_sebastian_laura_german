package com.tsdc.vinilos.domain.repositories

import com.tsdc.vinilos.domain.models.Artist

interface ArtistRepository {
    suspend fun getArtists(): List<Artist>
    suspend fun getArtistById(id: Int): Artist
    suspend fun isFavorite(artistId: Int): Boolean
    suspend fun toggleFavorite(artistId: Int)
}