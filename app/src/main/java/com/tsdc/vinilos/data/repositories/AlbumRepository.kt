package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.repositories.AlbumRepository as AlbumRepositoryInterface

class AlbumRepository(private val serviceAdapter: ServiceAdapter) : AlbumRepositoryInterface {
    override suspend fun getAlbums(): List<Album> = serviceAdapter.fetchAlbums()

    override suspend fun getAlbumById(id: Int): Album = serviceAdapter.getAlbumById(id)
}
