package com.tsdc.vinilos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tsdc.vinilos.data.local.entities.FavoriteArtistEntity

@Dao
interface FavoriteArtistDao {
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_artists WHERE artistId = :artistId)")
    suspend fun isFavorite(artistId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(entity: FavoriteArtistEntity)

    @Query("DELETE FROM favorite_artists WHERE artistId = :artistId")
    suspend fun removeFavorite(artistId: Int)
}
