package com.tsdc.vinilos.data.local

import androidx.room.TypeConverter
import java.util.Date

/**
 * Room no persiste [Date] sin convertidor explícito.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
