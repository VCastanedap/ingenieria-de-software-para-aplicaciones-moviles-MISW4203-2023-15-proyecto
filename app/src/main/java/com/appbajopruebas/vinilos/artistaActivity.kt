package com.appbajopruebas.vinilos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class artistaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artista)
        // Habilita el botón de retroceso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lista Artista"
    }
    // Esta función se llama cuando se presiona el botón de retroceso en la barra de acción
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