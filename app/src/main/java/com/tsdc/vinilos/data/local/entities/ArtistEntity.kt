package com.tsdc.vinilos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "musicians")
data class ArtistEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: Date
)
