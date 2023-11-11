package com.appbajopruebas.vinilos

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.appbajopruebas.vinilos.databinding.ActivityMainBinding
import com.appbajopruebas.vinilos.databinding.ActivityVinilosBinding

class VinilosActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vinilos)
        // Habilita el botón de retroceso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lista Vinilos"

        // conectar Fragment
        val binding = ActivityVinilosBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.navController
        // Make sure actions in the ActionBar get propagated to the NavController
        Log.d("act", navController.toString())
        setupActionBarWithNavController(navController)


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