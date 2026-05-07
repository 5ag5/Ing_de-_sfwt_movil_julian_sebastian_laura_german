package com.tsdc.vinilos.data.remote.network
import com.tsdc.vinilos.data.remote.dto.AlbumDto
import com.tsdc.vinilos.data.remote.dto.ArtistDto
import retrofit2.http.GET
import retrofit2.http.Path
import com.tsdc.vinilos.domain.models.Album

interface VinilosApiService {
    @GET("albums")
    suspend fun getAlbums(): List<AlbumDto>

    @GET("albums/{albumId}")
    suspend fun getAlbumById(
        @Path("albumId") albumId: Int
    ): Album

    @GET("musicians")
    suspend fun getArtists(): List<ArtistDto>
}