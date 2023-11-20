package com.appbajopruebas.vinilos.repositories

import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.network.CacheManager
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import android.app.Application
import android.util.Log
import com.appbajopruebas.vinilos.database.AlbumsDao
import com.appbajopruebas.vinilos.models.Collector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumRepository(val application: Application, private val albumsDao: AlbumsDao) {

    // Crear una variable que almacene una instancia de CacheManager
    private val cacheManager = CacheManager.getInstance(application.applicationContext)

    suspend fun refreshData(callback: (List<Album>) -> Unit, onError: (VolleyError) -> Unit) {
        withContext(Dispatchers.IO) {
            // Operaciones de la base de datos o red
            var cachedAlbums = cacheManager.getAlbums()

            if (cachedAlbums.isEmpty()) {
                Log.d("AlbumRepository", "No se encontraron álbumes en caché, verificando la base de datos")
                try {
                    val dbAlbums = albumsDao.getAlbums()
                    if (dbAlbums.isNotEmpty()) {
                        Log.d("AlbumRepository", "Devolviendo ${dbAlbums.size} álbumes de la base de datos")
                        callback(dbAlbums)
                    } else {
                        Log.d("AlbumRepository", "No hay álbumes en la base de datos, obteniendo de la red")
                        NetworkServiceAdapter.getInstance(application).getAlbums({
                            Log.d("AlbumRepository", "Obtenidos ${it.size} álbumes de la red")
                            cacheManager.addAlbums(it)
                            albumsDao.insert(it)
                            callback(it)
                        }, onError)
                    }
                } catch (error: VolleyError) {
                    Log.e("AlbumRepository", "Error al obtener álbumes de la base de datos: ${error.message}")
                    onError(error)
                }
            } else {
                Log.d("AlbumRepository", "Devolviendo ${cachedAlbums.size} álbumes de la caché")
                callback(cachedAlbums)
            }
        }
    }



}