package com.tsdc.vinilos.data.repositories.fake

import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.NewAlbum
import com.tsdc.vinilos.domain.models.Track
import com.tsdc.vinilos.domain.repositories.AlbumRepository

class FakeAlbumRepository : AlbumRepository {

    private val albums = mutableListOf(
        Album(
            id = 1,
            name = "Buscando America",
            cover = "https://picsum.photos/seed/album1/400/400",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Album de prueba para ejecucion en Robo Test.",
            genre = "Salsa",
            recordLabel = "EMI"
        ),
        Album(
            id = 2,
            name = "Poeta del Pueblo",
            cover = "https://picsum.photos/seed/album2/400/400",
            releaseDate = "1981-05-03T00:00:00.000Z",
            description = "Datos mock para validar navegacion sin backend.",
            genre = "Salsa",
            recordLabel = "Fania"
        )
    )

    private val tracksByAlbumId = mutableMapOf(
        1 to mutableListOf(
            Track(1, "Hijo de la Luna", "04:10"),
            Track(2, "Lluvia", "03:45")
        ),
        2 to mutableListOf(
            Track(3, "Volver", "03:58")
        )
    )

    override suspend fun getAlbums(): List<Album> = albums.toList()

    override suspend fun getAlbumById(id: Int): Album? = albums.firstOrNull { it.id == id }

    override suspend fun createAlbum(newAlbum: NewAlbum): Album {
        val nextId = (albums.maxOfOrNull { it.id } ?: 0) + 1
        val created = Album(
            id = nextId,
            name = newAlbum.name,
            cover = newAlbum.cover,
            releaseDate = newAlbum.releaseDateIso,
            description = newAlbum.description,
            genre = newAlbum.genre,
            recordLabel = newAlbum.recordLabel
        )
        albums.add(created)
        tracksByAlbumId[nextId] = mutableListOf()
        return created
    }

    override suspend fun getAlbumTracks(albumId: Int): List<Track> =
        tracksByAlbumId[albumId]?.toList() ?: emptyList()

    override suspend fun addTrackToAlbum(albumId: Int, name: String, duration: String): Track {
        if (albums.none { it.id == albumId }) {
            throw NoSuchElementException("Album with id $albumId was not found")
        }
        val nextTrackId = (tracksByAlbumId.values.flatten().maxOfOrNull { it.id } ?: 0) + 1
        val newTrack = Track(id = nextTrackId, name = name, duration = duration)
        val albumTracks = tracksByAlbumId.getOrPut(albumId) { mutableListOf() }
        albumTracks.add(newTrack)
        return newTrack
    }
}