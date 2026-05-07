package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.domain.repositories.ArtistRepository as ArtistRepositoryInterface

class ArtistRepository(private val serviceAdapter: ServiceAdapter): ArtistRepositoryInterface {
    override suspend fun getArtists(): List<Artist> = serviceAdapter.fetchArtists()
}