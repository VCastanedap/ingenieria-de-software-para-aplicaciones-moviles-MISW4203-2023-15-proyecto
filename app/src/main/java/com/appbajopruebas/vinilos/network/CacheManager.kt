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
    private var comments: HashMap<Int, List<Album>> = hashMapOf()
    fun addAlbum(albumId: Int, comment: List<Album>){
        if (!comments.containsKey(albumId)){
            comments[albumId] = comment
        }
    }
    fun getComments(albumId: Int) : List<Album>{
        return if (comments.containsKey(albumId)) comments[albumId]!! else listOf<Album>()
    }
}