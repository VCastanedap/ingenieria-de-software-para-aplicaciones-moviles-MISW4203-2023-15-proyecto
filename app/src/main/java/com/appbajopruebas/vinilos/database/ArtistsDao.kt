package com.appbajopruebas.vinilos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appbajopruebas.vinilos.models.Artist
import com.appbajopruebas.vinilos.models.Collector

@Dao
interface ArtistsDao {
    @Query("SELECT * FROM artists_table")
    fun getArtists(): List<Artist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(artists: List<Artist>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUno(artist: Artist)

    @Query("SELECT * FROM artists_table WHERE id = :artistId")
    fun getArtist(artistId: Int): Artist
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(artist: Artist) // Cambiado de List<Artist> a Artist
}
