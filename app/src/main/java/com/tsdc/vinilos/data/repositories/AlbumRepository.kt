package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.repositories.AlbumRepository as AlbumRepositoryInterface

class AlbumRepository(private val serviceAdapter: ServiceAdapter) : AlbumRepositoryInterface {
    override suspend fun getAlbums(): List<Album> = mockAlbums

    override suspend fun getAlbumById(id: Int): Album? = mockAlbums.find { it.id == id }
}

private val mockAlbums = listOf(
    Album(1, "Brat", "https://upload.wikimedia.org/wikipedia/en/6/6e/Charli_XCX_-_Brat.png", "2024-06-07", "Sexto álbum de Charli XCX", "Pop", "Atlantic Records"),
    Album(2, "The Dark Side of the Moon", "https://upload.wikimedia.org/wikipedia/en/3/3b/Dark_Side_of_the_Moon.png", "1973-03-01", "Álbum icónico de Pink Floyd", "Rock Progresivo", "Harvest Records"),
    Album(3, "Thriller", "https://upload.wikimedia.org/wikipedia/en/5/55/Michael_Jackson_-_Thriller.png", "1982-11-30", "El álbum más vendido de la historia", "Pop / R&B", "Epic Records"),
    Album(4, "Nevermind", "https://upload.wikimedia.org/wikipedia/en/b/b7/NirvanaNevermindalbumcover.jpg", "1991-09-24", "Segundo álbum de Nirvana", "Grunge", "DGC Records"),
    Album(5, "Abbey Road", "https://upload.wikimedia.org/wikipedia/en/4/42/Beatles_-_Abbey_Road.jpg", "1969-09-26", "Penúltimo álbum de The Beatles", "Rock", "Apple Records"),
    Album(6, "Rumours", "https://upload.wikimedia.org/wikipedia/en/f/f1/Fleetwood_Mac_-_Rumours.png", "1977-02-04", "Undécimo álbum de Fleetwood Mac", "Soft Rock", "Warner Bros.")
)