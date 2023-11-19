package com.appbajopruebas.vinilos.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appbajopruebas.vinilos.models.Collector

@Dao
interface CollectorsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(collectors: List<Collector>)

    @Query("SELECT * FROM collectors_table")
    fun getAllCollectors(): List<Collector>

    @Query("SELECT * FROM collectors_table WHERE id = :collectorId")
    fun getCollectorById(collectorId: Int): Collector?




}

