package com.appbajopruebas.vinilos

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
class ListArtistasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_artistas)
        // Habilita el botón de retroceso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lista Artista"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Finaliza esta actividad y vuelve a la anterior
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}