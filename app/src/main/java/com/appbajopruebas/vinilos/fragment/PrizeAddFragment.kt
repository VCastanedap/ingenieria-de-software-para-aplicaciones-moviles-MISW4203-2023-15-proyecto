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
import com.appbajopruebas.vinilos.databinding.FragmentPrizesAddBinding
import com.appbajopruebas.vinilos.models.Album
import com.appbajopruebas.vinilos.viewmodels.AlbumViewModel
import com.appbajopruebas.vinilos.ui.adapters.AlbumsAdapter
import com.appbajopruebas.vinilos.viewmodels.PrizesAddViewModel
import java.util.Calendar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */class PrizeAddFragment : Fragment() {

    private var _binding: FragmentPrizesAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PrizesAddViewModel

    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrizesAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        setupActionBar()

        val database = VinylRoomDatabase.getDatabase(requireActivity().application)
        val albumsDao = database.albumsDao()
        // Encontrar la referencia al botón "Agregar premio"
        val buttonAddPrize = binding.btnAddPrize

        buttonAddPrize.setOnClickListener {
            addPrize()
        }
        viewModel = ViewModelProvider(this, PrizesAddViewModel.Factory(requireActivity().application, albumsDao))
            .get(PrizesAddViewModel::class.java)



        observeViewModel()

    }

    private fun setupActionBar() {
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }


    private fun observeViewModel() {
        viewModel.eventPrizeCreated.observe(viewLifecycleOwner) {
            if (it) {
                showToastAndNavigate()
            }
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) {
            if (it) onNetworkError()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner, { message ->
            binding.textViewErrorMessage.text = message
            binding.textViewErrorMessage.visibility = if (message.isNotBlank()) View.VISIBLE else View.GONE
        })
    }

    private fun showToastAndNavigate() {
        Toast.makeText(context, "Premio creado", Toast.LENGTH_SHORT).show()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val navController = findNavController()
            navController.navigate(R.id.prizesAddFragment)  // Asegúrate de cambiar esto a tu destino correcto
        }, 2000)
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    private fun addPrize() {
        val textPrizeName = binding.editTextPrizeName.text.toString()
        val textPrizeOrganization = binding.editTextOrganization.text.toString()
        val textPrizeDescription = binding.editTextPrizeDescription.text.toString()

        viewModel.addPrize(
            textPrizeName,
            textPrizeOrganization,
            textPrizeDescription
        )
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
