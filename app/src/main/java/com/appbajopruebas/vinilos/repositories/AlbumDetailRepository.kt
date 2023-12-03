package com.appbajopruebas.vinilos.repositories

import android.content.Context
import android.util.Log
import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.network.CacheManager
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import com.appbajopruebas.vinilos.database.AlbumsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumDetailRepository(private val context: Context, private val albumsDao: AlbumsDao) {

    private val cacheManager = CacheManager.getInstance(context)

    suspend fun getAlbumDetails(
        albumId: Int,
        onComplete: suspend (Album) -> Unit,
        onError: suspend (VolleyError) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            // Verificar en caché
            val cachedAlbum = cacheManager.getAlbum(albumId)
            if (cachedAlbum != null) {
                Log.d("AlbumDetailRepository", "Devolviendo detalles del álbum desde la caché")
                onComplete(cachedAlbum)
                return@withContext
            }

            Log.d("AlbumDetailRepository", "No se encontraron detalles del álbum en caché, verificando la base de datos")
            try {
                // Verificar en la base de datos local
                val dbAlbum = albumsDao.getAlbum(albumId)
                if (dbAlbum != null) {
                    Log.d("AlbumDetailRepository", "Devolviendo detalles del álbum desde la base de datos local")
                    onComplete(dbAlbum)
                } else {
                    // Obtener detalles del álbum desde la red
                    Log.d("AlbumDetailRepository", "No se encontraron detalles del álbum en la base de datos, obteniendo de la red")
                    NetworkServiceAdapter.getInstance(context).getAlbumDetails(
                        albumId = albumId,
                        onComplete = { networkAlbum ->
                            Log.d("AlbumDetailRepository", "Detalles del álbum obtenidos de la red")
                            cacheManager.addAlbum(networkAlbum)
                            albumsDao.insertOne(networkAlbum)
                            onComplete(networkAlbum)
                        },
                        onError = { error ->
                            Log.e("AlbumDetailRepository", "Error al obtener detalles del álbum de la red: ${error.message}")
                            onError(error)
                        }
                    )
                }
            } catch (error: Exception) {
                Log.e("AlbumDetailRepository", "Error al obtener detalles del álbum de la base de datos local: ${error.message}")
                onError(VolleyError(error.message))
            }
        }
    }

    // Otras funciones relacionadas con los detalles del álbum pueden ir aquí
}
