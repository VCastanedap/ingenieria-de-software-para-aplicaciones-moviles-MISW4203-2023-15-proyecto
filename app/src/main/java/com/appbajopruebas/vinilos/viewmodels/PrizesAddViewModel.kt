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
import com.appbajopruebas.vinilos.models.Prize
import com.appbajopruebas.vinilos.repositories.PrizesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrizesAddViewModel(application: Application, albumsDao: AlbumsDao) : AndroidViewModel(application) {

    private val _eventPrizeCreated = MutableLiveData<Boolean>(false)
    val eventPrizeCreated: LiveData<Boolean>
        get() = _eventPrizeCreated

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _prizeRepository = PrizesRepository(application, albumsDao)

    fun addPrize(name: String, organization: String, description: String) {
        viewModelScope.launch {
            try {
                val success: Prize? = _prizeRepository.addPrize(name, organization, description)

                if (success is Prize) {
                    _eventPrizeCreated.value = true
                    _eventNetworkError.value = false
                    _isNetworkErrorShown.value = false
                } else {
                    _eventNetworkError.value = true
                    _errorMessage.value = "Hubo un error 400 al agregar el premio. Por favor, inténtalo de nuevo."
                }
            } catch (error: VolleyError) {
                _eventNetworkError.value = true
                _errorMessage.value = "Error de red. Por favor, verifica tu conexión a internet e inténtalo de nuevo."
            } catch (error: Exception) {
                // Manejar otras excepciones relacionadas con la base de datos
                _eventNetworkError.value = true
                _errorMessage.value = "Hubo un error en la base de datos. Por favor, inténtalo de nuevo."
            }
        }
    }
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }
    class Factory(val app: Application, private val albumsDao: AlbumsDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(PrizesAddViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PrizesAddViewModel(app, albumsDao) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
