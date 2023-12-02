package com.appbajopruebas.vinilos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appbajopruebas.vinilos.models.Collector

@Dao
interface CollectorDetailDao {
    @Query("SELECT * FROM collectors_table WHERE id = :collectorId")
    fun getCollectorDetails(collectorId: Int): Collector?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(collector: Collector)
}
