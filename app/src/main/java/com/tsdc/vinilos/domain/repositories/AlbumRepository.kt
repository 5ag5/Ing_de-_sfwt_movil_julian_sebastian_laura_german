package com.tsdc.vinilos.domain.repositories

import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.NewAlbum
import com.tsdc.vinilos.domain.models.Track

interface AlbumRepository {
    suspend fun getAlbums(): List<Album>
    suspend fun getAlbumById(id: Int): Album?
    suspend fun createAlbum(newAlbum: NewAlbum): Album
    suspend fun getAlbumTracks(albumId: Int): List<Track>
    suspend fun addTrackToAlbum(albumId: Int, name: String, duration: String): Track
}
