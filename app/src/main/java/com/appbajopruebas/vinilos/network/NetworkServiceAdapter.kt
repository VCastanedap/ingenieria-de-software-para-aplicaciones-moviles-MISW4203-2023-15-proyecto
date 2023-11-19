package com.appbajopruebas.vinilos.network
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.models.Artist
import com.appbajopruebas.vinilos.models.Collector
import com.appbajopruebas.vinilos.models.Comment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import org.json.JSONArray
import org.json.JSONObject

class NetworkServiceAdapter constructor(context: Context) {
    companion object{
        const val BASE_URL= "https://vynils-back-heroku.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }


    fun getAlbums(onComplete: suspend (resp: List<Album>) -> Unit, onError: suspend (error: VolleyError) -> Unit) {
        requestQueue.add(getRequest("albums",
            { response ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val resp = JSONArray(response)
                        val list = mutableListOf<Album>()
                        for (i in 0 until resp.length()) {
                            val item = resp.getJSONObject(i)
                            list.add(
                                i, Album(
                                    id = item.getInt("id"),
                                    name = item.getString("name"),
                                    cover = item.getString("cover"),
                                    recordLabel = item.getString("recordLabel"),
                                    releaseDate = item.getString("releaseDate"),
                                    genre = item.getString("genre"),
                                    description = item.getString("description")
                                )
                            )
                        }
                        if (list.isNotEmpty()) {
                            Log.d("BD", list[0].toString())
                        } else {
                            Log.d("BD", "No trae nada de BD")
                        }
                        onComplete(list)
                    } catch (error: Exception) {
                        // Manejar la excepción en caso de error
                        onError(VolleyError(error.message))
                    }
                }
            },
            {
                CoroutineScope(Dispatchers.IO).launch {
                    onError(it)
                }
            })
        )
    }
    fun getArtists(onComplete:(resp:List<Artist>)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(getRequest("musicians",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Artist>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i,
                        Artist(
                            id = item.getInt("id"),
                            name = item.getString("name"),
                            image = item.getString("image"),
                            description = item.getString("description"),
                            birthDate = item.getString("birthDate"),
                            albums = listOf(),
                            performerPrizes = listOf()
                        )
                    )
                }
                onComplete(list)
            },
            {
                onError(it)
            }))
    }
    fun getCollectors(onComplete:(resp:List<Collector>)->Unit, onError: (error:VolleyError)->Unit) {
        requestQueue.add(getRequest("collectors",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Collector>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Collector(id = item.getInt("id"),name = item.getString("name"), telephone = item.getString("telephone"), email = item.getString("email")
                        /*,   collectorAlbums = listOf(), comments = listOf(), favoritePerformers = listOf()*/))
                }
                onComplete(list)
            },
            {
                onError(it)
                Log.d("", it.message.toString())
            }))
    }

    fun getComments(albumId:Int, onComplete:(resp:List<Comment>)->Unit, onError: (error:VolleyError)->Unit) {
        requestQueue.add(getRequest("albums/$albumId/comments",
            Response.Listener<String> { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Comment>()
                var item:JSONObject? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    Log.d("Response", item.toString())
                    list.add(i, Comment(id = albumId, rating = item.getInt("rating").toString(), description = item.getString("description")))
                }
                onComplete(list)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }

    fun postComment(body: JSONObject, albumId: Int,  onComplete:(resp:JSONObject)->Unit , onError: (error:VolleyError)->Unit){
        requestQueue.add(postRequest("albums/$albumId/comments",
            body,
            Response.Listener<JSONObject> { response ->
                onComplete(response)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }
    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }
    private fun postRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.POST, BASE_URL+path, body, responseListener, errorListener)
    }
    private fun putRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.PUT, BASE_URL+path, body, responseListener, errorListener)
    }
}