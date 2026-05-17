package com.tsdc.vinilos.data.remote.network
import com.tsdc.vinilos.data.remote.dto.AlbumDto
import com.tsdc.vinilos.data.remote.dto.ArtistDto
import com.tsdc.vinilos.data.remote.dto.CollectorDto
import com.tsdc.vinilos.data.remote.dto.CreateAlbumRequest
import com.tsdc.vinilos.data.remote.dto.TrackDto
import com.tsdc.vinilos.data.remote.dto.TrackRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.tsdc.vinilos.domain.models.Album

interface VinilosApiService {
    @GET("albums")
    suspend fun getAlbums(): List<AlbumDto>

    @GET("albums/{albumId}")
    suspend fun getAlbumById(
        @Path("albumId") albumId: Int
    ): Album

    @POST("albums")
    suspend fun createAlbum(@Body body: CreateAlbumRequest): AlbumDto

    @GET("musicians")
    suspend fun getArtists(): List<ArtistDto>

    @GET("musicians/{artistId}")
    suspend fun getArtistById(
        @Path("artistId") artistId: Int
    ): ArtistDto

    @GET("collectors")
    suspend fun getCollectors(): List<CollectorDto>

    @GET("collectors/{collectorId}")
    suspend fun getCollectorById(
        @Path("collectorId") collectorId: Int
    ): CollectorDto

    @GET("albums/{albumId}/tracks")
    suspend fun getAlbumTracks(
        @Path("albumId") albumId: Int
    ): List<TrackDto>

    @POST("albums/{albumId}/tracks")
    suspend fun addTrackToAlbum(
        @Path("albumId") albumId: Int,
        @Body track: TrackRequest
    ): TrackDto
}