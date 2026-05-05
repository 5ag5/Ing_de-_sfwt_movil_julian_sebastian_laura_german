package com.tsdc.vinilos.domain.repositories

import com.tsdc.vinilos.domain.models.Artist

interface ArtistRepository {
    suspend fun getArtists(): List<Artist>
}