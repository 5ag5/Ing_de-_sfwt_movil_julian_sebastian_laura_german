package com.tsdc.vinilos.data.remote.network
import com.tsdc.vinilos.data.mappers.toDomain
import com.tsdc.vinilos.data.remote.dto.CreateAlbumRequest
import com.tsdc.vinilos.data.remote.dto.TrackRequest
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.domain.models.Track

class ServiceAdapter(private val apiService: VinilosApiService) {
    suspend fun fetchAlbums(): List<Album> = 
        apiService.getAlbums().map { it.toDomain() }

    suspend fun getAlbumById(albumId: Int): Album =
        apiService.getAlbumById(albumId).toDomain()

    suspend fun createAlbum(body: CreateAlbumRequest): Album =
        apiService.createAlbum(body).toDomain()

    suspend fun fetchArtists(): List<Artist> =
        apiService.getArtists().map { it.toDomain() }

    suspend fun fetchArtistById(artistId: Int): Artist =
        apiService.getArtistById(artistId).toDomain()

    suspend fun fetchCollectors(): List<Collector> =
        apiService.getCollectors().map { it.toDomain() }

    suspend fun fetchCollectorById(collectorId: Int): Collector =
        apiService.getCollectorById(collectorId).toDomain()

    suspend fun fetchAlbumTracks(albumId: Int): List<Track> =
        apiService.getAlbumTracks(albumId).map { it.toDomain() }

    suspend fun addTrackToAlbum(albumId: Int, name: String, duration: String): Track =
        apiService.addTrackToAlbum(albumId, TrackRequest(name, duration)).toDomain()
}