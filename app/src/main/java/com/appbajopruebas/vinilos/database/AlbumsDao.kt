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



}




