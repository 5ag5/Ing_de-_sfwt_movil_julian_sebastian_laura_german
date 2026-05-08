package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.local.dao.FavoriteArtistDao
import com.tsdc.vinilos.data.local.entities.FavoriteArtistEntity
import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.domain.repositories.ArtistRepository as ArtistRepositoryInterface

class ArtistRepository(
    private val serviceAdapter: ServiceAdapter,
    private val favoriteArtistDao: FavoriteArtistDao
) : ArtistRepositoryInterface {

    override suspend fun getArtists(): List<Artist> = serviceAdapter.fetchArtists()

    override suspend fun getArtistById(id: Int): Artist = serviceAdapter.fetchArtistById(id)

    override suspend fun isFavorite(artistId: Int): Boolean =
        favoriteArtistDao.isFavorite(artistId)

    override suspend fun toggleFavorite(artistId: Int) {
        if (favoriteArtistDao.isFavorite(artistId)) {
            favoriteArtistDao.removeFavorite(artistId)
        } else {
            favoriteArtistDao.addFavorite(FavoriteArtistEntity(artistId))
        }
    }
}
