package com.appbajopruebas.vinilos.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collectors_table")
data class Collector(
    //val collectorAlbums: List<CollectorAlbum>,
    //val comments: List<Comment>,
    val email: String,
  //  val favoritePerformers: List<Performer>,
    @PrimaryKey val id: Int,
    val name: String,
    val telephone: String
)