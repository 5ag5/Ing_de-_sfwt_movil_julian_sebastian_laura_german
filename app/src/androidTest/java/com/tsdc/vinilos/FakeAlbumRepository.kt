package com.tsdc.vinilos

import com.tsdc.vinilos.domain.models.NewAlbum
import com.tsdc.vinilos.domain.models.Track
import com.tsdc.vinilos.domain.repositories.AlbumRepository

/**
 * Catálogo fijo alineado con la biblioteca de ejemplo (pruebas sin backend).
 */
private val catalogAlbums = listOf(
    Album(
        id = 1,
        name = "Buscando América",
        cover = "",
        releaseDate = "1984-08-21",
        description = "",
        genre = "Salsa",
        recordLabel = "Elektra"
    ),
    Album(
        id = 2,
        name = "Poeta del pueblo",
        cover = "",
        releaseDate = "1984-08-21",
        description = "",
        genre = "Salsa",
        recordLabel = "Elektra"
    ),
    Album(
        id = 3,
        name = "A Night at the Opera",
        cover = "",
        releaseDate = "1975-11-21",
        description = "",
        genre = "Rock",
        recordLabel = "EMI"
    ),
    Album(
        id = 4,
        name = "A Day at the Races",
        cover = "",
        releaseDate = "1976-12-10",
        description = "",
        genre = "Rock",
        recordLabel = "EMI"
    )
)

class FakeAlbumRepository(
    private val albums: List<Album> = catalogAlbums
) : AlbumRepository {

    override suspend fun getAlbums(): List<Album> = albums

    override suspend fun getAlbumById(id: Int): Album? = albums.find { it.id == id }

    override suspend fun createAlbum(newAlbum: NewAlbum): Album {
        val nextId = (albums.maxOfOrNull { it.id } ?: 0) + 1
        return Album(
            id = nextId,
            name = newAlbum.name,
            cover = newAlbum.cover,
            releaseDate = newAlbum.releaseDateIso,
            description = newAlbum.description,
            genre = newAlbum.genre,
            recordLabel = newAlbum.recordLabel
        )
    }

    override suspend fun getAlbumTracks(albumId: Int): List<Track> = emptyList()

    override suspend fun addTrackToAlbum(albumId: Int, name: String, duration: String): Track =
        Track(id = 0, name = name, duration = duration)
}
