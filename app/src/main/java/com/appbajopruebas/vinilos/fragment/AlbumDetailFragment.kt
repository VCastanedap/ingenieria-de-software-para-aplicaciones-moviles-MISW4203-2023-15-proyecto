package com.appbajopruebas.vinilos.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.appbajopruebas.vinilos.R
import com.appbajopruebas.vinilos.VinilosActivity
import com.appbajopruebas.vinilos.database.VinylRoomDatabase
import com.appbajopruebas.vinilos.databinding.FragmentAlbumDetailBinding
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.viewmodels.AlbumDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class AlbumDetailFragment : Fragment() {
    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlbumDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el listener para el botón de retroceso
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Navegar hacia atrás al fragmento AlbumFragment
            findNavController().popBackStack()
        }
        // Obtén el ID del álbum del Bundle o argumentos
        val albumId = arguments?.getInt("albumId") ?: -1
        Log.d("Fragmento", "Album ID received: $albumId")

        // Configura el ViewModel
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        val database = VinylRoomDatabase.getDatabase(activity.application)
        val albumDao = database.albumsDao()



        viewModel = ViewModelProvider(this, AlbumDetailViewModel.Factory(activity.application,albumId, albumDao))
            .get(AlbumDetailViewModel::class.java)

        // Observa los cambios en el álbum
        viewModel.album.observe(viewLifecycleOwner, Observer<Album> { album ->
            // Actualiza la UI con los detalles del álbum
            // Cargar la imagen del álbum utilizando Glide
            Glide.with(requireContext())
                .load(album.cover.toUri().buildUpon().scheme("https").build())
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Configuración de la caché
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(binding.detailAlbumImageView)

            // Aquí puedes usar los datos de 'album' para mostrar la información en tu diseño
            binding.album = album
            Log.d("Fragmento", "Album details received: $album")
        })

        // Observa los cambios en el ID del álbum seleccionado
        viewModel.selectedAlbumId.observe(viewLifecycleOwner, Observer { selectedAlbumId ->
            if (selectedAlbumId != null) {
                // Cuando cambia el ID del álbum seleccionado, solicita los detalles del álbum
                viewModel.refreshDataFromNetwork() // Asegúrate de que esta función se esté llamando
                Log.d("Fragmento", "Selected Album ID changed to: $selectedAlbumId")
            }
        })

        // Observa los errores de red
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) {
                onNetworkError()
                Log.e("Fragmento", "Network error occurred")
            }
        })




    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
