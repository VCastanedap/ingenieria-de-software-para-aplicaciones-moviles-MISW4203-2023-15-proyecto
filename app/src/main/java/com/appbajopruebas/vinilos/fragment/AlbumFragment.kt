package com.appbajopruebas.vinilos.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbajopruebas.vinilos.MainActivity
import com.appbajopruebas.vinilos.R
import com.appbajopruebas.vinilos.VinilosActivity
import com.appbajopruebas.vinilos.database.VinylRoomDatabase
import com.appbajopruebas.vinilos.databinding.FragmentAlbumBinding
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.viewmodels.AlbumViewModel
import com.appbajopruebas.vinilos.ui.adapters.AlbumsAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AlbumFragment : Fragment() {
    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AlbumViewModel
    private var viewModelAdapter: AlbumsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.d("*** AlbumFragment", "_binding is null? ${_binding == null}")
        viewModelAdapter = AlbumsAdapter()
        // Configura datos dummy para realizar la prueba

        Log.d("*** AlbumFragment","Binding " )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.albumsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
        Log.d("*** fragment","recyclerView " )

// Configurar el listener para el botón de retroceso
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Acciones a realizar al presionar el botón de retroceso
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        })

        // Habilitar el botón de retroceso en la barra de acciones
        val mainActivity = requireActivity() as VinilosActivity
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mainActivity.supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val database = VinylRoomDatabase.getDatabase(activity.application)
        val albumDao = database.albumsDao()

        viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application,albumDao )).get(AlbumViewModel::class.java)
        viewModel.albums.observe(viewLifecycleOwner, Observer<List<Album>> {
            it.apply {
                viewModelAdapter!!.albums = this
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }


}
