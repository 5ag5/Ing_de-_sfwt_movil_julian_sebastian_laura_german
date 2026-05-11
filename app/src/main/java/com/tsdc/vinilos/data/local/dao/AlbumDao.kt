package com.tsdc.vinilos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tsdc.vinilos.data.local.entities.AlbumEntity

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums")
    suspend fun getAllAlbums(): List<AlbumEntity>

    @Query("SELECT * FROM albums WHERE id = :id LIMIT 1")
    suspend fun getAlbumById(id: Int): AlbumEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: AlbumEntity)

    @Query("DELETE FROM albums")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAlbums(albums: List<AlbumEntity>) {
        deleteAll()
        if (albums.isNotEmpty()) insertAlbums(albums)
    }
}