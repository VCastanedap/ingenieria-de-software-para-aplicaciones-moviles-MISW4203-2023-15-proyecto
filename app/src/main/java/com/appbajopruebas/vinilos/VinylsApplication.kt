package com.appbajopruebas.vinilos

import android.app.Application
import com.appbajopruebas.vinilos.database.VinylRoomDatabase

class VinylsApplication: Application()  {
    val database by lazy { VinylRoomDatabase.getDatabase(this) }
}