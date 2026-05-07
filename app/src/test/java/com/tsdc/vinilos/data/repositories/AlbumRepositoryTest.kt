package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.remote.dto.AlbumDto
import com.tsdc.vinilos.data.remote.dto.ArtistDto
import com.tsdc.vinilos.data.remote.dto.CollectorDto
import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.data.remote.network.VinilosApiService
import com.tsdc.vinilos.domain.models.Album
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AlbumRepositoryTest {

    @Test
    fun getAlbumById_with101_returnsExpectedAlbum() = runBlocking {
        val expectedAlbum = Album(
            id = 101,
            name = "Buscando America",
            cover = "https://example.com/covers/101.jpg",
            releaseDate = "1984-01-01",
            description = "Album de prueba",
            genre = "Salsa",
            recordLabel = "Fania"
        )

        val fakeApiService = FakeVinilosApiService(expectedAlbum)
        val repository = AlbumRepository(ServiceAdapter(fakeApiService))

        val result = repository.getAlbumById(101)

        assertNotNull(result)
        assertEquals(expectedAlbum, result)
        assertEquals(101, fakeApiService.lastRequestedAlbumId)
    }
}

private class FakeVinilosApiService(
    private val albumToReturn: Album
) : VinilosApiService {

    var lastRequestedAlbumId: Int? = null

    override suspend fun getAlbums(): List<AlbumDto> = emptyList()

    override suspend fun getAlbumById(albumId: Int): Album {
        lastRequestedAlbumId = albumId
        return albumToReturn
    }

    override suspend fun getArtists(): List<ArtistDto> = emptyList()

    override suspend fun getCollectors(): List<CollectorDto> = emptyList()
}
