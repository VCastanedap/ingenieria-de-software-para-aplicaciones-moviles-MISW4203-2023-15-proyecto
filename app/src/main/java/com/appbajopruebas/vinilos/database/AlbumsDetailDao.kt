package com.appbajopruebas.vinilos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.models.Collector

@Dao
interface AlbumsDetailDao {
    @Query("SELECT * FROM albums_table")
    fun getAlbumDetails(albumId: Int): List<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(albums: List<Album>)



}




