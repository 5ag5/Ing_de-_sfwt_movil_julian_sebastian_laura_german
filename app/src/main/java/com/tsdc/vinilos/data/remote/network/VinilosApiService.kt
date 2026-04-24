package com.tsdc.vinilos.data.remote.network
import com.tsdc.vinilos.data.remote.dto.AlbumDto
import retrofit2.http.GET

interface VinilosApiService {
    @GET("albums")
    suspend fun getAlbums(): List<AlbumDto>
}