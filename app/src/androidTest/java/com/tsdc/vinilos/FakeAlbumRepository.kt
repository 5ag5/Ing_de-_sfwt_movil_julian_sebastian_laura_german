package com.tsdc.vinilos

import com.tsdc.vinilos.domain.models.Album
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
}
