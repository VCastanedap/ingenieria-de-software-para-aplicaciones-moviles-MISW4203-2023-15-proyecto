package com.appbajopruebas.vinilos.fragment

import Collector
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.appbajopruebas.vinilos.database.VinylRoomDatabase
import com.appbajopruebas.vinilos.databinding.FragmentCollectorDetailBinding
import com.appbajopruebas.vinilos.models.Collector
import com.appbajopruebas.vinilos.viewmodels.CollectorDetailViewModel

class CollectorDetailFragment : Fragment() {
    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CollectorDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el listener para el botón de retroceso
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Navegar hacia atrás al fragmento CollectorFragment
            findNavController().popBackStack()
        }

        // Obtén el ID del coleccionista del Bundle o argumentos
        val collectorId = arguments?.getInt("collectorId") ?: -1
        // Log.d("Fragmento", "Collector ID received: $collectorId")

        // Configura el ViewModel
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        val database = VinylRoomDatabase.getDatabase(activity.application)
        val collectorDao = database.collectorsDao()

        viewModel = ViewModelProvider(this, CollectorDetailViewModel.Factory(activity.application, collectorId, collectorDao))
            .get(CollectorDetailViewModel::class.java)

        // Observa los cambios en el coleccionista
        viewModel.collector.observe(viewLifecycleOwner, Observer<Collector> { collector ->
            // Actualiza la UI con los detalles del coleccionista
            // Cargar la imagen del coleccionista utilizando Glide
            /*Glide.with(requireContext())
                .load(collector.imageUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Configuración de la caché
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(binding.detailCollectorImageView)*/

            // Aquí puedes usar los datos de 'collector' para mostrar la información en tu diseño
            binding.collector = collector
            // Log.d("Fragmento", "Collector details received: $collector")
        })

        // Observa los cambios en el ID del coleccionista seleccionado
        viewModel.selectedCollectorId.observe(viewLifecycleOwner, Observer { selectedCollectorId ->
            if (selectedCollectorId != null) {
                // Cuando cambia el ID del coleccionista seleccionado, solicita los detalles del coleccionista
                viewModel.refreshDataFromNetwork() // Asegúrate de que esta función se esté llamando
                // Log.d("Fragmento", "Selected Collector ID changed to: $selectedCollectorId")
            }
        })

        // Observa los errores de red
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) {
                onNetworkError()
                // Log.e("Fragmento", "Network error occurred")
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
