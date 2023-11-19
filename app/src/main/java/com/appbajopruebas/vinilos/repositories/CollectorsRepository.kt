package com.appbajopruebas.vinilos.repositories

import com.android.volley.VolleyError
import com.appbajopruebas.vinilos.network.NetworkServiceAdapter
import android.app.Application
import android.util.Log
import com.appbajopruebas.vinilos.models.Collector

class CollectorsRepository (val application: Application){
    fun refreshData(callback: (List<Collector>)->Unit, onError: (VolleyError)->Unit) {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        Log.d("*** Repositoy","refreshData")
        NetworkServiceAdapter.getInstance(application).getCollectors({
            //Guardar los albumes de la variable it en un almacén de datos local para uso futuro

            callback(it)
        },
            onError
        )
    }


}