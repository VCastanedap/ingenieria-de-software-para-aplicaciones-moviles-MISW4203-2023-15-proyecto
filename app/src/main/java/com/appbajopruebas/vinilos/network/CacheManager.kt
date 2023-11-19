package com.appbajopruebas.vinilos.network

import android.content.Context
import com.appbajopruebas.vinilos.models.Album

class CacheManager(context: Context) {
    companion object{
        var instance: CacheManager? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: CacheManager(context).also {
                    instance = it
                }
            }
    }

    //Cambiar el tipo de la variable album a una lista de álbumes
    private var album: List<Album> = listOf()

    //Modificar el método addAlbums para que reciba solo una lista de álbumes
    fun addAlbums(albums: List<Album>){
        //Asignar la lista de álbumes a la variable album
        album = albums
    }
    //Modificar el método getAlbums para que no reciba ningún parámetro
    fun getAlbums() : List<Album>{
        //Retornar la variable album
        return album
    }
}
