package com.appbajopruebas.vinilos.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.database.AlbumsDao
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.repositories.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlbumAddViewModel(application: Application, albumsDao: AlbumsDao) : AndroidViewModel(application) {

    private val _albums = MutableLiveData<List<Album>>()
    private val _albumRepository = AlbumRepository(application, albumsDao)

    val albums: LiveData<List<Album>>
        get() = _albums

    private val _eventAlbumCreated = MutableLiveData<Boolean>(false)

    val eventAlbumCreated: LiveData<Boolean>
        get() = _eventAlbumCreated

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        Log.d("***ViewModel", "Creo el viewmodel")
        viewModelScope.launch {
            refreshDataFromNetwork()
        }
    }

    private suspend fun refreshDataFromNetwork() {
        try {
            _albumRepository.refreshData(
                callback = { albums ->
                    // Actualizar LiveData en el hilo principal
                    viewModelScope.launch(Dispatchers.Main) {
                        _albums.value = albums
                        _eventNetworkError.value = false
                        _isNetworkErrorShown.value = false
                    }
                }
            ) { error ->
                // Actualizar LiveData en el hilo principal
                viewModelScope.launch(Dispatchers.Main) {
                    _eventNetworkError.value = true
                }
            }
        } catch (error: VolleyError) {
            // Actualizar LiveData en el hilo principal
            viewModelScope.launch(Dispatchers.Main) {
                _eventNetworkError.value = true
            }
        }
    }

    fun addAlbum(name: String, cover: String, releaseDate: String, description: String, genre: String, recordLabel: String) {
        viewModelScope.launch {
            val success: Album? = _albumRepository.addAlbum(name, cover, releaseDate, description, genre, recordLabel)

            if (success is Album) {
                _eventAlbumCreated.value = true
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } else {
                _eventNetworkError.value = true
            }
        }
    }




    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application, private val albumsDao: AlbumsDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(AlbumAddViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumAddViewModel(app, albumsDao) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}