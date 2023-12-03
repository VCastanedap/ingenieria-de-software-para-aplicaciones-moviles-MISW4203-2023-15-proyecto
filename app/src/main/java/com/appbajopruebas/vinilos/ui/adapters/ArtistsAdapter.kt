package com.appbajopruebas.vinilos.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.appbajopruebas.vinilos.R
import com.appbajopruebas.vinilos.databinding.ArtistItemBinding
import com.appbajopruebas.vinilos.fragment.ArtistFragmentDirections
import com.appbajopruebas.vinilos.models.Artist
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class ArtistsAdapter : RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>(){

    var artists :List<Artist> = emptyList()
        set(value) {
            field = value
            Log.d("*** adapater","Artists " )
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val withDataBinding: ArtistItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ArtistViewHolder.LAYOUT,
            parent,
            false)
        Log.d("ArtistsAdapter", "se crea? on "+ parent.context.toString() )
        return ArtistViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.artist = artists[position]
            // Cargar la imagen desde la URL
            Glide.with(holder.itemView.context)
                .load(artists[position].image)
                .into(it.imageViewCard)
        }
        holder.viewDataBinding.root.setOnClickListener {
            Log.d("ArtistsAdapter", "Clicked on ${artists[position].name}")

            // Obtén el NavController desde la vista y navega al detalle del álbum
            val action = ArtistFragmentDirections.actionArtistFragmentToArtistDetailFragment(artists[position].id)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        Log.d("*** getItemCount", artists.size.toString() )
        return artists.size
    }


    class ArtistViewHolder(val viewDataBinding: ArtistItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.artist_item
        }

        @SuppressLint("ResourceType")
        fun bind(artist: Artist) {
            Glide.with(itemView)
                .load(artist.image.toUri().buildUpon().scheme("https").build())
                .apply(RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_broken_image))
                .into(viewDataBinding.imageViewCard)
        }
    }


}
