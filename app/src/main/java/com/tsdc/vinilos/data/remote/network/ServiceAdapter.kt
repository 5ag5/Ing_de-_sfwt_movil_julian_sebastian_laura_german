package com.tsdc.vinilos.data.remote.network
import com.tsdc.vinilos.data.mappers.toDomain
import com.tsdc.vinilos.domain.models.Album

class ServiceAdapter(private val apiService: VinilosApiService) {
    suspend fun fetchAlbums(): List<Album> = 
        apiService.getAlbums().map { it.toDomain() }

    suspend fun getAlbumById(albumId: Int): Album {
        return apiService.getAlbumById(albumId)
    }
}