package com.tsdc.vinilos.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.tsdc.vinilos.data.local.entities.ArtistEntity

@Dao
interface ArtistDao {
    @Query("SELECT * FROM musicians")
    suspend fun getAllArtists(): List<ArtistEntity>
}