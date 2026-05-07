package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.remote.dto.AlbumDto
import com.tsdc.vinilos.data.remote.dto.ArtistDto
import com.tsdc.vinilos.data.remote.dto.CollectorDto
import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.data.remote.network.VinilosApiService
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.Artist
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class ArtistRepositoryTest {

    @Test
    fun getArtists_returnsMappedListFromApi() = runBlocking {
        val birth = Date(449_606_400_000L) // ~1984-04-28
        val dtos = listOf(
            ArtistDto(
                id = 201,
                name = "Rubén Blades",
                image = "https://example.com/img.jpg",
                description = "Cantautor",
                birthDate = birth
            ),
            ArtistDto(
                id = 202,
                name = "Otros",
                image = "",
                description = "Desc",
                birthDate = birth
            )
        )
        val expected = listOf(
            Artist(201, "Rubén Blades", "https://example.com/img.jpg", "Cantautor", birth),
            Artist(202, "Otros", "", "Desc", birth)
        )

        val fakeApi = FakeVinilosApiServiceForArtists(dtos)
        val repository = ArtistRepository(ServiceAdapter(fakeApi))

        val result = repository.getArtists()

        assertTrue(fakeApi.wasGetArtistsCalled)
        assertEquals(expected, result)
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

    override suspend fun getCollectors(): List<CollectorDto> = emptyList()
}
