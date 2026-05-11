package com.tsdc.vinilos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tsdc.vinilos.data.local.entities.ArtistEntity

@Dao
interface ArtistDao {
    @Query("SELECT * FROM musicians")
    suspend fun getAllArtists(): List<ArtistEntity>

    @Query("SELECT * FROM musicians WHERE id = :id LIMIT 1")
    suspend fun getArtistById(id: Int): ArtistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(items: List<ArtistEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artist: ArtistEntity)

    @Query("DELETE FROM musicians")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceArtists(items: List<ArtistEntity>) {
        deleteAll()
        if (items.isNotEmpty()) insertArtists(items)
    }
}