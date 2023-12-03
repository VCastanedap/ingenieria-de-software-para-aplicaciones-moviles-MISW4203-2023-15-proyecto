package com.appbajopruebas.vinilos.models

import java.lang.annotation.ElementType
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists_table")
data class Artist (
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String,
    // val albums: List<Album>,
    // val performerPrizes: List<ElementType>,
)
