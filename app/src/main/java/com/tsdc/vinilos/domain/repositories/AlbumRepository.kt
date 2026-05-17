package com.tsdc.vinilos.domain.repositories

import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.NewAlbum

interface AlbumRepository {
    suspend fun getAlbums(): List<Album>
    suspend fun getAlbumById(id: Int): Album?
    suspend fun createAlbum(newAlbum: NewAlbum): Album
}
