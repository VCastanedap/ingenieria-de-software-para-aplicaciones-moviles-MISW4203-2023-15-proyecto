package com.appbajopruebas.vinilos.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.appbajopruebas.vinilos.database.CollectorsDao
import com.appbajopruebas.vinilos.models.Collector
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CollectorsRepository(private val application: Application, private val collectorsDao: CollectorsDao) {

    suspend fun refreshCollectors(): List<Collector> {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        // Log para verificar si hay conexión a la red
        Log.d("CollectorsRepository", "Está conectado a la red: $isConnected")

        return if (isConnected) {
            val collectorsFromNetwork: List<Collector>? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).getCollectors(
                    { collectors ->
                        // Log para indicar que se recibieron los coleccionistas de la red
                        Log.d("CollectorsRepository", "Recibidos los coleccionistas de la red")
                        continuation.resume(collectors)
                    },
                    { error ->
                        // Log en caso de error al obtener coleccionistas de la red
                        Log.d("CollectorsRepository", "Error al obtener coleccionistas de la red")
                        continuation.resume(null)
                    }
                )
            }

            collectorsFromNetwork?.let { collectors ->
                withContext(Dispatchers.IO) {
                    // Log para indicar que se están insertando coleccionistas en la base de datos
                    Log.d("CollectorsRepository", "Insertando coleccionistas en la base de datos")
                    collectorsDao.insert(collectors)
                }
                collectors
            } ?: run {
                // Log en caso de que los coleccionistas de la red sean nulos, devolver lista vacía
                Log.d("CollectorsRepository", "Los coleccionistas de la red son nulos, devolver lista vacía")
                emptyList()
            }
        } else {
            return withContext(Dispatchers.IO) {
                val cachedCollectors = collectorsDao.getAllCollectors()
                // Log para indicar que se devuelven los coleccionistas almacenados en la base de datos
                Log.d("CollectorsRepository", "Devolviendo coleccionistas almacenados en la base de datos")
                cachedCollectors ?: emptyList()
            }
        }
    }



}
