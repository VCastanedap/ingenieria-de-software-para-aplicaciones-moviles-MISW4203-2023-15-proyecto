package com.appbajopruebas.vinilos.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.appbajopruebas.vinilos.database.CollectorsDao
import com.appbajopruebas.vinilos.models.Collector
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import com.appbajopruebas.vinilos.repositories.CollectorsRepository
import kotlinx.coroutines.launch


class CollectorViewModel(application: Application,collectorsDao: CollectorsDao) :  AndroidViewModel(application) {

    private val _collectors = MutableLiveData<List<Collector>?>()
    val _collectorsRepository = CollectorsRepository(application, collectorsDao)
    val collectors: MutableLiveData<List<Collector>?>
        get() = _collectors

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private lateinit var collectorsRepository: CollectorsRepository
    init {
              refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        viewModelScope.launch {
            var data: List<Collector>? = _collectorsRepository.refreshCollectors()

            if(data != null) {
                _collectors.postValue(data)
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

    class Factory(val app: Application, val collectorsDao: CollectorsDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorViewModel(app, collectorsDao) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}