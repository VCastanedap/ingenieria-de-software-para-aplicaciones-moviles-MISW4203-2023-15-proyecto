package com.appbajopruebas.vinilos.models

import java.lang.annotation.ElementType

data class Artist (
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String,
    val albums: List<Album>,
    val performerPrizes: List<ElementType>,
)



