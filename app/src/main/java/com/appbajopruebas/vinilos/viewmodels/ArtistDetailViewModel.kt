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
import com.appbajopruebas.vinilos.database.ArtistsDao
import com.appbajopruebas.vinilos.models.Artist
import com.appbajopruebas.vinilos.repositories.ArtistDetailRepository
import com.appbajopruebas.vinilos.repositories.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistDetailViewModel(application: Application, artistId: Int, artistsDao: ArtistsDao) : AndroidViewModel(application) {

    private val _artist = MutableLiveData<Artist>()
    private val _artistDetailRepository = ArtistDetailRepository(application, artistsDao)
    private val _selectedArtistId = MutableLiveData<Int>(artistId)
    val artist: LiveData<Artist>
        get() = _artist

    val selectedArtistId: LiveData<Int>
        get() = _selectedArtistId

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
                Log.d("ArtistDetailViewModel", "se creo")
                _artistDetailRepository.getArtistDetails(
                    artistId = _selectedArtistId.value ?: 0,
                    onComplete = { data ->
                        // Actualiza el LiveData en el hilo principal
                        withContext(Dispatchers.Main) {
                            _artist.value = data
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


    class Factory(private val application: Application, private val artistId: Int, private val artistDao: ArtistsDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistDetailViewModel(application, artistId, artistDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
