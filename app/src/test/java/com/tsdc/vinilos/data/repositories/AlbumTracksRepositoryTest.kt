package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.remote.dto.AlbumDto
import com.tsdc.vinilos.data.remote.dto.ArtistDto
import com.tsdc.vinilos.data.remote.dto.CollectorDto
import com.tsdc.vinilos.data.remote.dto.CreateAlbumRequest
import com.tsdc.vinilos.data.remote.dto.TrackDto
import com.tsdc.vinilos.data.remote.dto.TrackRequest
import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.data.remote.network.VinilosApiService
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.Track
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AlbumTracksRepositoryTest {

    private val sampleTracks = listOf(
        TrackDto(id = 1, name = "Decisiones", duration = "5:05"),
        TrackDto(id = 2, name = "Desapariciones", duration = "6:29")
    )

    private val expectedTracks = listOf(
        Track(id = 1, name = "Decisiones", duration = "5:05"),
        Track(id = 2, name = "Desapariciones", duration = "6:29")
    )

    @Test
    fun getAlbumTracks_returnsMappedListFromApi() = runBlocking {
        val fakeApi = FakeVinilosApiServiceForTracks(tracksToReturn = sampleTracks)
        val repository = AlbumRepository(ServiceAdapter(fakeApi), FakeAlbumDaoForTracks())

        val result = repository.getAlbumTracks(100)

        assertTrue(fakeApi.wasGetAlbumTracksCalled)
        assertEquals(expectedTracks, result)
    }

    @Test
    fun addTrackToAlbum_returnsCreatedTrack() = runBlocking {
        val newTrack = TrackDto(id = 3, name = "Pedro Navaja", duration = "7:52")
        val fakeApi = FakeVinilosApiServiceForTracks(trackToAdd = newTrack)
        val repository = AlbumRepository(ServiceAdapter(fakeApi), FakeAlbumDaoForTracks())

        val result = repository.addTrackToAlbum(100, "Pedro Navaja", "7:52")

        assertTrue(fakeApi.wasAddTrackCalled)
        assertEquals(Track(id = 3, name = "Pedro Navaja", duration = "7:52"), result)
    }
}

private class FakeVinilosApiServiceForTracks(
    private val tracksToReturn: List<TrackDto> = emptyList(),
    private val trackToAdd: TrackDto = TrackDto(0, "", "")
) : VinilosApiService {

    var wasGetAlbumTracksCalled = false
        private set

    var wasAddTrackCalled = false
        private set

    override suspend fun getAlbums(): List<AlbumDto> = emptyList()

    override suspend fun getAlbumById(albumId: Int): Album {
        error("no usado en test de tracks")
    }

    override suspend fun getArtists(): List<ArtistDto> = emptyList()

    override suspend fun getArtistById(artistId: Int): ArtistDto {
        error("no usado en test de tracks")
    }

    override suspend fun getCollectors(): List<CollectorDto> = emptyList()

    override suspend fun getCollectorById(collectorId: Int): CollectorDto {
        error("no usado en test de tracks")
    }

    override suspend fun createAlbum(body: CreateAlbumRequest): AlbumDto {
        error("no usado en test de tracks")
    }

    override suspend fun getAlbumTracks(albumId: Int): List<TrackDto> {
        wasGetAlbumTracksCalled = true
        return tracksToReturn
    }

    override suspend fun addTrackToAlbum(albumId: Int, track: TrackRequest): TrackDto {
        wasAddTrackCalled = true
        return trackToAdd
    }
}

private class FakeAlbumDaoForTracks : com.tsdc.vinilos.data.local.dao.AlbumDao {
    override suspend fun getAllAlbums() = emptyList<com.tsdc.vinilos.data.local.entities.AlbumEntity>()
    override suspend fun getAlbumById(id: Int) = null
    override suspend fun insertAlbums(albums: List<com.tsdc.vinilos.data.local.entities.AlbumEntity>) {}
    override suspend fun insert(album: com.tsdc.vinilos.data.local.entities.AlbumEntity) {}
    override suspend fun deleteAll() {}
}
