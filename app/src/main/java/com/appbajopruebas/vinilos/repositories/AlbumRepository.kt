package com.appbajopruebas.vinilos.repositories

import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.network.CacheManager
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import android.app.Application
import android.util.Log

class AlbumRepository (val application: Application){

    //Crear una variable que almacene una instancia de CacheManager
    private val cacheManager = CacheManager.getInstance(application.applicationContext)

    fun refreshData(callback: (List<Album>)->Unit, onError: (VolleyError)->Unit) {
        //Verificar si los álbumes ya están en la caché
        var cachedAlbums = cacheManager.getAlbums()
        if (cachedAlbums.isEmpty()){
            //Si la caché está vacía, hacer la petición de red
            Log.d("Cache decision", "get from network")
            NetworkServiceAdapter.getInstance(application).getAlbums({
                //Guardar los álbumes en la caché
                cacheManager.addAlbums(it)
                callback(it) //Usar el mismo nombre que el parámetro
            },
                onError
            )
        }
        else{
            Log.d("Cache decision", "return ${cachedAlbums.size} elements from cache")
            //Si la caché tiene los álbumes, devolverlos sin hacer la petición de red
            callback(cachedAlbums) //Usar el mismo nombre que el parámetro
        }
    }
}
