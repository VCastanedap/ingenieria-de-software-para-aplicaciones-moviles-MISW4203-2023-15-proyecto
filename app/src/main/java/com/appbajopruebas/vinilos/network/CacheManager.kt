package com.appbajopruebas.vinilos.network

import android.content.Context
import com.appbajopruebas.vinilos.models.Album

class CacheManager private constructor(context: Context) {

    companion object {
        private var instance: CacheManager? = null

        fun getInstance(context: Context): CacheManager =
            instance ?: synchronized(this) {
                instance ?: CacheManager(context).also {
                    instance = it
                }
            }
    }

    // Cambiar el tipo de la variable albums a una mutableList de álbumes
    private var albums: MutableList<Album> = mutableListOf()

    // Modificar el método addAlbums para que reciba solo una lista de álbumes
    fun addAlbums(albums: List<Album>) {
        // Limpiar la lista actual antes de agregar los nuevos álbumes
        this.albums.clear()
        this.albums.addAll(albums)
    }

    // Modificar el método getAlbums para que no reciba ningún parámetro
    fun getAlbums(): List<Album> {
        // Retornar una copia inmutable de la lista de álbumes
        return albums.toList()
    }

    // Añadir método para obtener un álbum por su ID
    fun getAlbumById(albumId: Int): Album? {
        return albums.find { it.id == albumId }
    }

    // Añadir método para agregar un álbum individual
    fun addAlbum(album: Album) {
        // Verificar si el álbum ya está en la lista
        val existingAlbum = albums.find { it.id == album.id }
        if (existingAlbum == null) {
            // Si no existe, agregarlo a la lista
            val updatedList = albums.toMutableList()
            updatedList.add(album)
            this.albums = updatedList
        } else {
            // Si ya existe, actualizar sus detalles
            val updatedList = albums.map {
                if (it.id == album.id) {
                    album
                } else {
                    it
                }
            }.toMutableList()
            this.albums = updatedList
        }
    }

    // Añadir método para obtener un álbum por su ID
    fun getAlbum(albumId: Int): Album? {
        return albums.find { it.id == albumId }
    }
}
