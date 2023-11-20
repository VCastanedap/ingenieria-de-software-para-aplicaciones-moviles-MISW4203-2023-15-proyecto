package com.appbajopruebas.vinilos.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbajopruebas.vinilos.R
import com.appbajopruebas.vinilos.databinding.FragmentArtistBinding
import com.appbajopruebas.vinilos.models.Artist
import com.appbajopruebas.vinilos.viewmodels.ArtistViewModel
import com.appbajopruebas.vinilos.ui.adapters.ArtistsAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ArtistFragment : Fragment() {
    private var _binding: FragmentArtistBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ArtistViewModel
    private var viewModelAdapter: ArtistsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = ArtistsAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.artistsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("*** ArtistFragment","Binding " )
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, ArtistViewModel.Factory(activity.application)).get(ArtistViewModel::class.java)
        viewModel.artists.observe(viewLifecycleOwner, Observer<List<Artist>> {
            it.apply {
                viewModelAdapter!!.artists = this
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