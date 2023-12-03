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
import com.appbajopruebas.vinilos.database.CollectorsDao
import com.appbajopruebas.vinilos.models.Collector
import com.appbajopruebas.vinilos.repositories.CollectorDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectorDetailViewModel(application: Application, collectorId: Int, collectorsDao: CollectorsDao) : AndroidViewModel(application) {

    private val _collector = MutableLiveData<Collector>()
    private val _collectorDetailRepository = CollectorDetailRepository(application, collectorsDao)
    private val _selectedCollectorId = MutableLiveData<Int>(collectorId)
    val collector: LiveData<Collector>
        get() = _collector

    val selectedCollectorId: LiveData<Int>
        get() = _selectedCollectorId

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
                Log.d("ColecDetailVM", "se creo")
                _collectorDetailRepository.getCollectorDetails(
                    collectorId = _selectedCollectorId.value ?: 0,
                    onComplete = { data ->
                        // Actualiza el LiveData en el hilo principal
                        withContext(Dispatchers.Main) {
                            _collector.value = data
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

    class Factory(private val application: Application, private val collectorId: Int, private val collectorsDao: CollectorsDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorDetailViewModel(application, collectorId, collectorsDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
