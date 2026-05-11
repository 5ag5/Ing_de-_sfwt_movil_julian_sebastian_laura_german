package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.local.dao.CollectorDao
import com.tsdc.vinilos.data.remote.dto.AlbumDto
import com.tsdc.vinilos.data.remote.dto.ArtistDto
import com.tsdc.vinilos.data.remote.dto.CollectorDto
import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.data.remote.network.VinilosApiService
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.Collector
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectorRepositoryTest {

    @Test
    fun getCollectors_returnsMappedListFromApi() = runBlocking {
        val dtos = listOf(
            CollectorDto(301, "Ana", "3001112233", "ana@mail.com"),
            CollectorDto(302, "Luis", "3004445566", "luis@mail.com")
        )
        val expected = listOf(
            Collector(301, "Ana", "3001112233", "ana@mail.com"),
            Collector(302, "Luis", "3004445566", "luis@mail.com")
        )

        val fakeApi = FakeVinilosApiServiceForCollectors(dtos)
        val collectorDao = mockk<CollectorDao>(relaxed = true)
        val repository = CollectorRepository(ServiceAdapter(fakeApi), collectorDao)

        val result = repository.getCollectors()

        assertTrue(fakeApi.wasGetCollectorsCalled)
        assertEquals(expected, result)
    }
}

private class FakeVinilosApiServiceForCollectors(
    private val collectorsToReturn: List<CollectorDto>
) : VinilosApiService {

    var wasGetCollectorsCalled: Boolean = false
        private set

    override suspend fun getAlbums(): List<AlbumDto> = emptyList()

    override suspend fun getAlbumById(albumId: Int): Album {
        error("no usado en test de coleccionistas")
    }

    override suspend fun getArtists(): List<ArtistDto> = emptyList()

    override suspend fun getArtistById(artistId: Int): ArtistDto {
        error("no usado en test de coleccionistas")
    }

    override suspend fun getCollectors(): List<CollectorDto> {
        wasGetCollectorsCalled = true
        return collectorsToReturn
    }
}
