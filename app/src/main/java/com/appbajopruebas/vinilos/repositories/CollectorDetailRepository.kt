package com.appbajopruebas.vinilos.repositories

import android.content.Context
import android.util.Log
import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.models.Collector
import com.appbajopruebas.vinilos.network.CacheManager
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import com.appbajopruebas.vinilos.database.CollectorsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CollectorDetailRepository(private val context: Context, private val collectorsDao: CollectorsDao) {

    private val cacheManager = CacheManager.getInstance(context)

    suspend fun getCollectorDetails(
        collectorId: Int,
        onComplete: suspend (Collector) -> Unit,
        onError: suspend (VolleyError) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            // Verificar en caché
            withContext(Dispatchers.IO) {
                // Verificar en caché
                val cachedCollector = cacheManager.getCollectorById(collectorId)
                if (cachedCollector != null) {
                    Log.d("ColecDetailRepo", "Devolviendo detalles del coleccionista desde la caché")
                    onComplete(cachedCollector)
                    return@withContext
                }

                // Resto del código...
            }


            Log.d("ColecDetailRepo", "No se encontraron detalles del coleccionista en caché, verificando la base de datos")
            try {
                // Verificar en la base de datos local
                val dbCollector = collectorsDao.getCollectorById(collectorId)
                if (dbCollector != null) {
                    Log.d("ColecDetailRepo", "Devolviendo detalles del coleccionista desde la base de datos local")
                    onComplete(dbCollector)
                } else {
                    // Obtener detalles del coleccionista desde la red
                    Log.d("ColecDetailRepo", "No se encontraron detalles del coleccionista en la base de datos, obteniendo de la red")
                    NetworkServiceAdapter.getInstance(context).getCollectorDetails(
                        collectorId = collectorId,
                        onComplete = { networkCollector ->
                            Log.d("ColecDetailRepo", "Detalles del coleccionista obtenidos de la red")
                            cacheManager.addCollector(networkCollector)
                            collectorsDao.insert(networkCollector)
                            onComplete(networkCollector)
                        },
                        onError = { error ->
                            Log.e("ColecDetailRepo", "Error al obtener detalles del coleccionista de la red: ${error.message}")
                            onError(error)
                        }
                    )
                }
            } catch (error: Exception) {
                Log.e("ColecDetailRepo", "Error al obtener detalles del coleccionista de la base de datos local: ${error.message}")
                onError(VolleyError(error.message))
            }
        }
    }

    // Otras funciones relacionadas con los detalles del coleccionista pueden ir aquí
}
