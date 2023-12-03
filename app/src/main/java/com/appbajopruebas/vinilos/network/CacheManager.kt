package com.appbajopruebas.vinilos.network

import android.content.Context
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.models.Artist
import com.appbajopruebas.vinilos.models.Collector

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

    // Lista mutable de álbumes
    private var albums: MutableList<Album> = mutableListOf()

    // Lista mutable de coleccionistas
    private var collectors: MutableList<Collector> = mutableListOf()

    // Lista mutable de artistas
    private var artists: MutableList<Artist> = mutableListOf()

    // Métodos para álbumes
    fun addAlbums(albums: List<Album>) {
        this.albums.clear()
        this.albums.addAll(albums)
    }

    fun getAlbums(): List<Album> {
        return albums.toList()
    }

    fun getAlbumById(albumId: Int): Album? {
        return albums.find { it.id == albumId }
    }

    fun addAlbum(album: Album) {
        val existingAlbum = albums.find { it.id == album.id }
        if (existingAlbum == null) {
            val updatedList = albums.toMutableList()
            updatedList.add(album)
            this.albums = updatedList
        } else {
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

    fun getAlbum(albumId: Int): Album? {
        return albums.find { it.id == albumId }
    }

    // Métodos para coleccionistas
    fun addCollectors(collectors: List<Collector>) {
        this.collectors.clear()
        this.collectors.addAll(collectors)
    }

    fun getCollectors(): List<Collector> {
        return collectors.toList()
    }

    fun getCollectorById(collectorId: Int): Collector? {
        return collectors.find { it.id == collectorId }
    }

    fun addCollector(collector: Collector) {
        val existingCollector = collectors.find { it.id == collector.id }
        if (existingCollector == null) {
            val updatedList = collectors.toMutableList()
            updatedList.add(collector)
            this.collectors = updatedList
        } else {
            val updatedList = collectors.map {
                if (it.id == collector.id) {
                    collector
                } else {
                    it
                }
            }.toMutableList()
            this.collectors = updatedList
        }
    }

    // Métodos para artistas
    fun addArtists(artists: List<Artist>) {
        this.artists.clear()
        this.artists.addAll(artists)
    }

    fun getArtists(): List<Artist> {
        return artists.toList()
    }

    fun getArtistsById(artistId: Int): Artist? {
        return artists.find { it.id == artistId }
    }

    fun addArtist(artist: Artist) {
        val existingArtist = artists.find { it.id == artist.id }
        if (existingArtist == null) {
            val updatedList = artists.toMutableList()
            updatedList.add(artist)
            this.artists = updatedList
        } else {
            val updatedList = artists.map {
                if (it.id == artist.id) {
                    artist
                } else {
                    it
                }
            }.toMutableList()
            this.artists = updatedList
        }
    }
}
