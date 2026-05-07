package com.tsdc.vinilos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tsdc.vinilos.data.local.dao.AlbumDao
import com.tsdc.vinilos.data.local.dao.ArtistDao
import com.tsdc.vinilos.data.local.entities.AlbumEntity
import com.tsdc.vinilos.data.local.entities.ArtistEntity

@Database(entities = [AlbumEntity::class, ArtistEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vinilos_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
