package com.tsdc.vinilos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tsdc.vinilos.data.local.dao.AlbumDao
import com.tsdc.vinilos.data.local.dao.ArtistDao
import com.tsdc.vinilos.data.local.dao.FavoriteArtistDao
import com.tsdc.vinilos.data.local.entities.AlbumEntity
import com.tsdc.vinilos.data.local.entities.ArtistEntity
import com.tsdc.vinilos.data.local.entities.FavoriteArtistEntity

@Database(
    entities = [AlbumEntity::class, ArtistEntity::class, FavoriteArtistEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao
    abstract fun favoriteArtistDao(): FavoriteArtistDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `favorite_artists` (`artistId` INTEGER NOT NULL, PRIMARY KEY(`artistId`))"
                )
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vinilos_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
