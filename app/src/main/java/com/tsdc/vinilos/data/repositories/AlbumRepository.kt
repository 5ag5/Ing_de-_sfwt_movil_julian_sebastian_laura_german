package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.local.dao.AlbumDao
import com.tsdc.vinilos.data.mappers.toDomain
import com.tsdc.vinilos.data.mappers.toEntity
import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.repositories.AlbumRepository as AlbumRepositoryInterface

class AlbumRepository(
    private val serviceAdapter: ServiceAdapter,
    private val albumDao: AlbumDao
) : AlbumRepositoryInterface {

    override suspend fun getAlbums(): List<Album> =
        try {
            val remote = serviceAdapter.fetchAlbums()
            albumDao.replaceAlbums(remote.map { it.toEntity() })
            remote
        } catch (e: Exception) {
            val cached = albumDao.getAllAlbums().map { it.toDomain() }
            if (cached.isEmpty()) throw e
            cached
        }

    override suspend fun getAlbumById(id: Int): Album? =
        try {
            val remote = serviceAdapter.getAlbumById(id)
            albumDao.insert(remote.toEntity())
            remote
        } catch (_: Exception) {
            albumDao.getAlbumById(id)?.toDomain()
        }
}
