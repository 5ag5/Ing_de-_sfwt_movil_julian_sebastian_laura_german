package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.local.dao.ArtistDao
import com.tsdc.vinilos.data.local.dao.FavoriteArtistDao
import com.tsdc.vinilos.data.local.entities.FavoriteArtistEntity
import com.tsdc.vinilos.data.mappers.toDomain
import com.tsdc.vinilos.data.mappers.toEntity
import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.domain.repositories.ArtistRepository as ArtistRepositoryInterface

class ArtistRepository(
    private val serviceAdapter: ServiceAdapter,
    private val favoriteArtistDao: FavoriteArtistDao,
    private val artistDao: ArtistDao
) : ArtistRepositoryInterface {

    override suspend fun getArtists(): List<Artist> =
        try {
            val remote = serviceAdapter.fetchArtists()
            artistDao.replaceArtists(remote.map { it.toEntity() })
            remote
        } catch (e: Exception) {
            val cached = artistDao.getAllArtists().map { it.toDomain() }
            if (cached.isEmpty()) throw e
            cached
        }

    override suspend fun getArtistById(id: Int): Artist =
        try {
            val remote = serviceAdapter.fetchArtistById(id)
            artistDao.insert(remote.toEntity())
            remote
        } catch (e: Exception) {
            artistDao.getArtistById(id)?.toDomain() ?: throw e
        }

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
