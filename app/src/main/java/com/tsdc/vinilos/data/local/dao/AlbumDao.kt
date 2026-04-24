package com.tsdc.vinilos.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.tsdc.vinilos.domain.models.Album

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums") 
    fun getAllAlbums(): List<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albums: List<Album>)
}