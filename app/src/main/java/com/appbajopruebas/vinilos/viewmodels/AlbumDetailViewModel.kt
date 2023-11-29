package com.appbajopruebas.vinilos.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.database.AlbumsDao
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.repositories.AlbumDetailRepository
import com.appbajopruebas.vinilos.repositories.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailViewModel(application: Application, albumId: Int, albumsDao: AlbumsDao) : AndroidViewModel(application) {

    private val _album = MutableLiveData<Album>()
    private val _albumDetailRepository = AlbumDetailRepository(application, albumsDao)
    private val _selectedAlbumId = MutableLiveData<Int>(albumId)
    val album: LiveData<Album>
        get() = _album

    val selectedAlbumId: LiveData<Int>
        get() = _selectedAlbumId

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    fun refreshDataFromNetwork() {
        viewModelScope.launch {
            try {
                Log.d("AlbumDetailViewModel", "se creo")
                _albumDetailRepository.getAlbumDetails(
                    albumId = _selectedAlbumId.value ?: 0,
                    onComplete = { data ->
                        // Actualiza el LiveData en el hilo principal
                        withContext(Dispatchers.Main) {
                            _album.value = data
                            _eventNetworkError.value = false
                            _isNetworkErrorShown.value = false
                        }
                    },
                    onError = { _ ->
                        _eventNetworkError.value = true
                    }
                )
            } catch (error: VolleyError) {
                _eventNetworkError.value = true
            }
        }
    }


    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }


    class Factory(private val application: Application, private val albumId: Int, private val albumDao: AlbumsDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumDetailViewModel(application, albumId, albumDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
