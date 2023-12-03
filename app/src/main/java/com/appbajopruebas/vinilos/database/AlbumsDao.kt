package com.appbajopruebas.vinilos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.models.Collector

@Dao
interface AlbumsDao {
    @Query("SELECT * FROM albums_table")
    fun getAlbums(): List<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(albums: List<Album>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUno(album: Album)

    @Query("SELECT * FROM albums_table WHERE id = :albumId")
    fun getAlbum(albumId: Int): Album
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(album: Album) // Cambiado de List<Album> a Album
}
