package com.appbajopruebas.vinilos.repositories

import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.network.CacheManager
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.appbajopruebas.vinilos.database.AlbumsDao
import com.appbajopruebas.vinilos.models.Collector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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


    suspend fun addAlbum(name: String, cover: String, releaseDate: String, description: String, genre: String, recordLabel: String): Album? {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        if(isConnected) {
            val albumToSend = Album(0, name, cover, releaseDate, description, genre, recordLabel)
            val album = JSONObject()
            album.put("name", albumToSend.name)
            album.put("cover", albumToSend.cover)
            album.put("releaseDate", albumToSend.releaseDate)
            album.put("description", albumToSend.description)
            album.put("genre", albumToSend.genre)
            album.put("recordLabel", albumToSend.recordLabel)
            val albumFromNetwork: Album? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).addAlbum(album,
                    { album ->
                        continuation.resume(album)
                    }
                ) {
                    continuation.resume(null)
                }
            }

            albumFromNetwork?.let { album ->
                withContext(Dispatchers.IO) {
                    albumsDao.insertUno(album)
                }
                return album
            }

        }
        return null
    }






}