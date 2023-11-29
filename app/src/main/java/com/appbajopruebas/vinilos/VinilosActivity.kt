package com.appbajopruebas.vinilos

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.appbajopruebas.vinilos.databinding.ActivityMainBinding
import com.appbajopruebas.vinilos.databinding.ActivityVinilosBinding

class VinilosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVinilosBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el objeto de vinculación
        binding = ActivityVinilosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Resto del código de tu actividad...
        supportActionBar?.title = "Lista Vinilos"

        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.navController
        // Make sure actions in the ActionBar get propagated to the NavController
        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Habilita el botón de retroceso en la ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Cuando se hace clic en el botón de retroceso, inicia MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}