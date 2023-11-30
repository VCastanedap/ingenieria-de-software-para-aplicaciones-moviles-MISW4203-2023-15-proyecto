package com.appbajopruebas.vinilos.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.appbajopruebas.vinilos.R
import com.appbajopruebas.vinilos.databinding.CollectorItemBinding
import com.appbajopruebas.vinilos.fragment.AlbumFragmentDirections
import com.appbajopruebas.vinilos.fragment.CollectorFragmentDirections
import com.appbajopruebas.vinilos.models.Collector
import com.bumptech.glide.Glide

class CollectorsAdapter : RecyclerView.Adapter<CollectorsAdapter.CollectorViewHolder>(){

    var collectors :List<Collector> = emptyList()
        set(value) {
            field = value
            Log.d("*** adapater","albums " )
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val withDataBinding: CollectorItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorViewHolder.LAYOUT,
            parent,
            false)
        Log.d("AlbumsAdapter", "se crea? on "+ parent.context.toString() )
        return CollectorViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collector = collectors[position]

        }
        holder.viewDataBinding.root.setOnClickListener {
            Log.d("CollectorsAdapter", "Clicked on ${collectors[position].name}")

            // Obtén el NavController desde la vista y navega al detalle del coleccionista
            val action = CollectorFragmentDirections.actionCollectorFragmentToCollectorDetailFragment(collectors[position].id)
            it.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        Log.d("*** getItemCount", collectors.size.toString() )
        return collectors.size
    }


    class CollectorViewHolder(val viewDataBinding: CollectorItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.collector_item
        }
    }


}
