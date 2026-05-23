package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.local.dao.ArtistDao
import com.tsdc.vinilos.data.local.dao.FavoriteArtistDao
import com.tsdc.vinilos.data.local.entities.FavoriteArtistEntity
import com.tsdc.vinilos.data.remote.dto.AlbumDto
import com.tsdc.vinilos.data.remote.dto.ArtistDto
import com.tsdc.vinilos.data.remote.dto.CollectorDto
import com.tsdc.vinilos.data.remote.dto.CreateAlbumRequest
import com.tsdc.vinilos.data.remote.dto.TrackDto
import com.tsdc.vinilos.data.remote.dto.TrackRequest
import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.data.remote.network.VinilosApiService
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.Artist
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class ArtistRepositoryTest {

    private val birth = Date(449_606_400_000L)

    private val sampleDtos = listOf(
        ArtistDto(id = 201, name = "Rubén Blades", image = "https://example.com/img.jpg", description = "Cantautor", birthDate = birth),
        ArtistDto(id = 202, name = "Otros", image = "", description = "Desc", birthDate = birth)
    )

    private val expectedArtists = listOf(
        Artist(201, "Rubén Blades", "https://example.com/img.jpg", "Cantautor", birth),
        Artist(202, "Otros", "", "Desc", birth)
    )

    private val artistDaoMock: ArtistDao = mockk(relaxed = true)

    @Test
    fun getArtists_returnsMappedListFromApi() = runBlocking {
        val fakeApi = FakeVinilosApiServiceForArtists(sampleDtos)
        val repository =
            ArtistRepository(ServiceAdapter(fakeApi), FakeFavoriteArtistDao(), artistDaoMock)

        val result = repository.getArtists()

        assertTrue(fakeApi.wasGetArtistsCalled)
        assertEquals(expectedArtists, result)
    }

    @Test
    fun getArtistById_returnsMappedArtistFromApi() = runBlocking {
        val fakeApi = FakeVinilosApiServiceForArtists(sampleDtos)
        val repository =
            ArtistRepository(ServiceAdapter(fakeApi), FakeFavoriteArtistDao(), artistDaoMock)

        val result = repository.getArtistById(201)

        assertEquals(expectedArtists[0], result)
    }

    @Test
    fun toggleFavorite_addsThenRemovesFavorite() = runBlocking {
        val repository =
            ArtistRepository(ServiceAdapter(FakeVinilosApiServiceForArtists(sampleDtos)), FakeFavoriteArtistDao(), artistDaoMock)

        assertFalse(repository.isFavorite(201))
        repository.toggleFavorite(201)
        assertTrue(repository.isFavorite(201))
        repository.toggleFavorite(201)
        assertFalse(repository.isFavorite(201))
    }
}

private class FakeVinilosApiServiceForArtists(
    private val artistsToReturn: List<ArtistDto>
) : VinilosApiService {

    var wasGetArtistsCalled: Boolean = false
        private set

    override suspend fun getAlbums(): List<AlbumDto> = emptyList()

    override suspend fun getAlbumById(albumId: Int): Album {
        error("no usado en test de artistas")
    }

    override suspend fun getArtists(): List<ArtistDto> {
        wasGetArtistsCalled = true
        return artistsToReturn
    }

    override suspend fun getArtistById(artistId: Int): ArtistDto {
        return artistsToReturn.first { it.id == artistId }
    }

    override suspend fun getCollectors(): List<CollectorDto> = emptyList()

    override suspend fun getCollectorById(collectorId: Int): CollectorDto {
        error("no usado en test de artistas")
    }

    override suspend fun createAlbum(body: CreateAlbumRequest): AlbumDto {
        error("no usado en test de artistas")
    }

    override suspend fun getAlbumTracks(albumId: Int): List<TrackDto> = emptyList()

    override suspend fun addTrackToAlbum(albumId: Int, track: TrackRequest): TrackDto {
        error("no usado en test de artistas")
    }
}

private class FakeFavoriteArtistDao : FavoriteArtistDao {
    private val favorites = mutableSetOf<Int>()

    override suspend fun isFavorite(artistId: Int): Boolean = favorites.contains(artistId)

    override suspend fun addFavorite(entity: FavoriteArtistEntity) {
        favorites.add(entity.artistId)
    }

    override suspend fun removeFavorite(artistId: Int) {
        favorites.remove(artistId)
    }
}
