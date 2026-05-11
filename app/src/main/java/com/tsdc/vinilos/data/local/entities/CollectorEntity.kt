package com.tsdc.vinilos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collectors")
data class CollectorEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val telephone: String,
    val email: String
)
