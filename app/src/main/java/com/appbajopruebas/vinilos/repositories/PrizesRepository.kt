package com.appbajopruebas.vinilos.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.database.AlbumsDao
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.models.Prize
import com.appbajopruebas.vinilos.network.CacheManager
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PrizesRepository(val application: Application, private val albumsDao: AlbumsDao) {

    // Crear una variable que almacene una instancia de CacheManager
    private val cacheManager = CacheManager.getInstance(application.applicationContext)



    suspend fun addPrize(name: String, organization: String, description: String): Prize? {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        if (isConnected) {
            val prizeToSend = Prize(0, name, organization, description)
            val prize = JSONObject()
            prize.put("name", prizeToSend.name)
            prize.put("organization", prizeToSend.organization)
            prize.put("description", prizeToSend.description)

            val prizeFromNetwork: Prize? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).addPrize(prize,
                    { prize ->
                        continuation.resume(prize)
                    }
                ) {
                    continuation.resume(null)
                }
            }


        }
        return null
    }
}
