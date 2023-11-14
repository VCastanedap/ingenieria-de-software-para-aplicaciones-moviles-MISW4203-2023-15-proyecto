package com.appbajopruebas.vinilos.models

data class Collector(
    val collectorAlbums: List<CollectorAlbum>,
    val comments: List<Comment>,
    val email: String,
    val favoritePerformers: List<Performer>,
    val id: Int,
    val name: String,
    val telephone: String
)