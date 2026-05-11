package com.tsdc.vinilos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tsdc.vinilos.data.local.entities.CollectorEntity

@Dao
interface CollectorDao {
    @Query("SELECT * FROM collectors")
    suspend fun getAllCollectors(): List<CollectorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectors(items: List<CollectorEntity>)

    @Query("DELETE FROM collectors")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceCollectors(items: List<CollectorEntity>) {
        deleteAll()
        if (items.isNotEmpty()) insertCollectors(items)
    }
}
