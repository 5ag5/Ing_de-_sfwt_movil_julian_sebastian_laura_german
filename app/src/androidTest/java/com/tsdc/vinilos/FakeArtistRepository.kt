package com.tsdc.vinilos

import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.domain.repositories.ArtistRepository
import java.util.Calendar
import java.util.Date

private fun date(year: Int, month: Int, day: Int): Date =
    Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time


private val sampleArtists = listOf(
    Artist(
        id = 100,
        name = "Rubén Blades Bellido de Luna",
        image = "",
        description = "Es un cantante, compositor y músico panameño.",
        birthDate = date(1948, 7, 16)
    ),
    Artist(
        id = 101,
        name = "Queen",
        image = "",
        description = "Banda británica de rock formada en Londres.",
        birthDate = date(1970, 1, 1)
    )
)

class FakeArtistRepository(
    private val artists: List<Artist> = sampleArtists
) : ArtistRepository {

    override suspend fun getArtists(): List<Artist> = artists
}
