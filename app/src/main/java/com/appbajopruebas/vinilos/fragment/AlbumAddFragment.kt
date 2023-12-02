package com.appbajopruebas.vinilos.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbajopruebas.vinilos.MainActivity
import com.appbajopruebas.vinilos.R
import com.appbajopruebas.vinilos.VinilosActivity
import com.appbajopruebas.vinilos.database.VinylRoomDatabase
import com.appbajopruebas.vinilos.databinding.FragmentAlbumAddBinding
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.viewmodels.AlbumViewModel
import com.appbajopruebas.vinilos.ui.adapters.AlbumsAdapter
import java.util.Calendar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AlbumAddFragment:Fragment() {

    private var _binding: FragmentAlbumAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlbumViewModel

    private val _eventAlbumCreated = MutableLiveData<Boolean>(false)

    val eventAlbumCreated: LiveData<Boolean>
        get() = _eventAlbumCreated

    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumAddBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val buttonAddAlbum = binding.btnAddAlbum

        val editReleaseDate = binding.editTextReleaseDate


        // Habilitar el botón de retroceso en el fragmento
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar el comportamiento del botón de retroceso en el fragmento
        setHasOptionsMenu(true)


        val database = VinylRoomDatabase.getDatabase(activity.application)
        val albumsDao = database.albumsDao()


        viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application, albumsDao)).get(AlbumViewModel::class.java)

        // Obtener la fecha actual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(
            requireActivity(), // Cambio aquí: Utilizar requireActivity() en lugar de this
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editReleaseDate.setText(selectedDate)
            },
            year,
            month,
            day
        )

        editReleaseDate.isFocusable = false
        editReleaseDate.isClickable = true

        viewModel.eventAlbumCreated.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Album creado", Toast.LENGTH_SHORT).show()

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    val navController = findNavController()

                    // Navega al fragmento deseado después del retraso
                    navController.navigate(R.id.albumFragment)
                }, 2000)
            }
        }

        editReleaseDate.setOnClickListener {
            datePickerDialog.show()
        }

        buttonAddAlbum.setOnClickListener {
            addAlbum()
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) {
            if (it) onNetworkError()
        }

        // Encontrar la referencia al TextView
        val textViewErrorMessage: TextView = view.findViewById(R.id.textViewErrorMessage)


        // Observar el mensaje de error
        viewModel.errorMessage.observe(viewLifecycleOwner, { message ->
            // Mostrar el mensaje en el TextView
            textViewErrorMessage.text = message


            // Opcional: Mostrar o ocultar el TextView según si hay un mensaje de error
            textViewErrorMessage.visibility = if (message.isNotBlank()) View.VISIBLE else View.GONE
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addAlbum() {
        val textAlbumName = binding.editTextAlbumName.text.toString()
        val textAlbumCover = binding.editTextAlbumCover.text.toString()
        val textAlbumReleaseDate = binding.editTextReleaseDate.text.toString()
        val textAlbumDescription = binding.editTextDescription.text.toString()
        val textAlbumGenre = binding.spinnerGenre.selectedItem.toString()
        val textAlbumRecordLabel = binding.spinnerRecordLabel.selectedItem.toString()


        viewModel.addAlbum(
            textAlbumName,
            textAlbumCover,
            textAlbumReleaseDate,
            textAlbumDescription,
            textAlbumGenre,
            textAlbumRecordLabel
        )
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Manejar el evento de clic en el botón de retroceso
                // Puedes personalizar el comportamiento según tus necesidades
                requireActivity().onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}