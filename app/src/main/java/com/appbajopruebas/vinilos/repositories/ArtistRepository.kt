package com.appbajopruebas.vinilos.repositories

import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.models.Artist
import com.appbajopruebas.vinilos.network.CacheManager
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.appbajopruebas.vinilos.database.ArtistsDao
import com.appbajopruebas.vinilos.models.Collector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ArtistRepository(val application: Application, private val artistsDao: ArtistsDao) {

    // Crear una variable que almacene una instancia de CacheManager
    private val cacheManager = CacheManager.getInstance(application.applicationContext)

    suspend fun refreshData(callback: (List<Artist>) -> Unit, onError: (VolleyError) -> Unit) {
        withContext(Dispatchers.IO) {
            // Operaciones de la base de datos o red
            var cachedArtists = cacheManager.getArtists()

            if (cachedArtists.isEmpty()) {
                Log.d("ArtistRepository", "No se encontraron álbumes en caché, verificando la base de datos")
                try {
                    val dbArtists = artistsDao.getArtists()
                    if (dbArtists.isNotEmpty()) {
                        Log.d("ArtistRepository", "Devolviendo ${dbArtists.size} álbumes de la base de datos")
                        callback(dbArtists)
                    } else {
                        Log.d("ArtistRepository", "No hay álbumes en la base de datos, obteniendo de la red")
                        NetworkServiceAdapter.getInstance(application).getArtists({
                            Log.d("ArtistRepository", "Obtenidos ${it.size} álbumes de la red")
                            cacheManager.addArtists(it)
                            artistsDao.insert(it)
                            callback(it)
                        }, onError)
                    }
                } catch (error: VolleyError) {
                    Log.e("ArtistRepository", "Error al obtener álbumes de la base de datos: ${error.message}")
                    onError(error)
                }
            } else {
                Log.d("ArtistRepository", "Devolviendo ${cachedArtists.size} álbumes de la caché")
                callback(cachedArtists)
            }
        }
    }
}