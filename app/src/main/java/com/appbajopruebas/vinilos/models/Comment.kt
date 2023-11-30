package com.appbajopruebas.vinilos.models
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments_table",
    foreignKeys = [
        ForeignKey(
            entity = Collector::class,
            parentColumns = ["id"],
            childColumns = ["collectorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Comment(
    @PrimaryKey val id: Int,
    val description: String,
    val rating: String,
    val collectorId: Int // ID del coleccionista al que pertenece este comentario
)
