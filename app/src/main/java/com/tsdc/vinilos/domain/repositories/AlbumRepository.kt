package com.tsdc.vinilos.domain.repositories

import com.tsdc.vinilos.domain.models.Album

interface AlbumRepository {
    suspend fun getAlbums(): List<Album>
    suspend fun getAlbumById(id: Int): Album?
}
