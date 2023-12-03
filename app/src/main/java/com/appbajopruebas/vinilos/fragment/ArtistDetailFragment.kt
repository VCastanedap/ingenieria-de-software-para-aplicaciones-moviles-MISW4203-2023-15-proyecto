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
import com.appbajopruebas.vinilos.database.VinylRoomDatabase
import com.appbajopruebas.vinilos.databinding.FragmentArtistDetailBinding
import com.appbajopruebas.vinilos.models.Artist
import com.appbajopruebas.vinilos.viewmodels.ArtistDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ArtistDetailFragment : Fragment() {
    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ArtistDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el listener para el botón de retroceso
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Navegar hacia atrás al fragmento ArtistFragment
            findNavController().popBackStack()
        }
        // Obtén el ID del artista del Bundle o argumentos
        val artistId = arguments?.getInt("artistId") ?: -1
        Log.d("Fragmento", "Artist ID received: $artistId")

        // Configura el ViewModel
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        val database = VinylRoomDatabase.getDatabase(activity.application)
        val artistDao = database.artistsDao()

        viewModel = ViewModelProvider(this, ArtistDetailViewModel.Factory(activity.application,artistId, artistDao))
            .get(ArtistDetailViewModel::class.java)

        // Observa los cambios en el artista
        viewModel.artist.observe(viewLifecycleOwner, Observer<Artist> { artist ->
            // Actualiza la UI con los detalles del artista
            // Cargar la imagen del artista utilizando Glide
            Glide.with(requireContext())
                .load(artist.image.toUri().buildUpon().scheme("https").build())
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Configuración de la caché
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(binding.detailArtistImageView)

            // Aquí puedes usar los datos de 'artist' para mostrar la información en tu diseño
            binding.artist = artist
            Log.d("Fragmento", "Artist details received: $artist")
        })

        // Observa los cambios en el ID del artista seleccionado
        viewModel.selectedArtistId.observe(viewLifecycleOwner, Observer { selectedArtistId ->
            if (selectedArtistId != null) {
                // Cuando cambia el ID del artista seleccionado, solicita los detalles del artista
                viewModel.refreshDataFromNetwork() // Asegúrate de que esta función se esté llamando
                Log.d("Fragmento", "Selected Artist ID changed to: $selectedArtistId")
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
