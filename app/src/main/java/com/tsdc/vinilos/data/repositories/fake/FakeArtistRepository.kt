package com.tsdc.vinilos.data.repositories.fake

import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.domain.repositories.ArtistRepository
import java.util.Date

class FakeArtistRepository : ArtistRepository {

    private val artists = listOf(
        Artist(
            id = 1,
            name = "Hector Lavoe",
            image = "https://picsum.photos/seed/artist1/400/400",
            description = "Artista mock para pruebas Robo.",
            birthDate = Date(315532800000L)
        ),
        Artist(
            id = 2,
            name = "Celia Cruz",
            image = "https://picsum.photos/seed/artist2/400/400",
            description = "Datos simulados para navegacion y detalle.",
            birthDate = Date(-781920000000L)
        )
    )

    private val favoriteIds = mutableSetOf<Int>()

    override suspend fun getArtists(): List<Artist> = artists

    override suspend fun getArtistById(id: Int): Artist =
        artists.firstOrNull { it.id == id }
            ?: throw NoSuchElementException("Artist with id $id was not found")

    override suspend fun isFavorite(artistId: Int): Boolean = artistId in favoriteIds

    override suspend fun toggleFavorite(artistId: Int) {
        if (artistId in favoriteIds) {
            favoriteIds.remove(artistId)
        } else {
            favoriteIds.add(artistId)
        }
    }
}