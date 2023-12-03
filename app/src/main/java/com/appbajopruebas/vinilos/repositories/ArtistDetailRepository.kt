package com.appbajopruebas.vinilos.repositories

import android.content.Context
import android.util.Log
import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.models.Artist
import com.appbajopruebas.vinilos.network.CacheManager
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import com.appbajopruebas.vinilos.database.ArtistsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArtistDetailRepository(private val context: Context, private val artistsDao: ArtistsDao) {

    private val cacheManager = CacheManager.getInstance(context)

    suspend fun getArtistDetails(
        artistId: Int,
        onComplete: suspend (Artist) -> Unit,
        onError: suspend (VolleyError) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            // Verificar en caché
            val cachedArtist = cacheManager.getArtistsById(artistId)
            if (cachedArtist != null) {
                Log.d("ArtistDetailRepository", "Devolviendo detalles del artista desde la caché")
                onComplete(cachedArtist)
                return@withContext
            }

            Log.d("ArtistDetailRepository", "No se encontraron detalles del artista en caché, verificando la base de datos")
            try {
                // Verificar en la base de datos local
                val dbArtist = artistsDao.getArtist(artistId)
                if (dbArtist != null) {
                    Log.d("ArtistDetailRepository", "Devolviendo detalles del artista desde la base de datos local")
                    onComplete(dbArtist)
                } else {
                    // Obtener detalles del artista desde la red
                    Log.d("ArtistDetailRepository", "No se encontraron detalles del artista en la base de datos, obteniendo de la red")
                    NetworkServiceAdapter.getInstance(context).getArtistDetails(
                        artistId = artistId,
                        onComplete = { networkArtist ->
                            Log.d("ArtistDetailRepository", "Detalles del artista obtenidos de la red")
                            cacheManager.addArtist(networkArtist)
                            artistsDao.insertOne(networkArtist)
                            onComplete(networkArtist)
                        },
                        onError = { error ->
                            Log.e("ArtistDetailRepository", "Error al obtener detalles del artista de la red: ${error.message}")
                            onError(error)
                        }
                    )
                }
            } catch (error: Exception) {
                Log.e("ArtistDetailRepository", "Error al obtener detalles del artista de la base de datos local: ${error.message}")
                onError(VolleyError(error.message))
            }
        }
    }

    // Otras funciones relacionadas con los detalles del artista pueden ir aquí
}
