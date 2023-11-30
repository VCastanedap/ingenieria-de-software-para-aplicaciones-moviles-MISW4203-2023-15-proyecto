package com.appbajopruebas.vinilos.models

import CollectorAlbum
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "collectors_table")
data class Collector(
    @PrimaryKey val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
     // Relación uno a muchos con Album
    @Relation(parentColumn = "id", entityColumn = "collectorId")
    val collectorAlbums: List<CollectorAlbum>,

    // Relación uno a muchos con Comment]
    /*    @Relation(parentColumn = "id", entityColumn = "collectorId")
        val comments: List<Comment>,
    */

    )
