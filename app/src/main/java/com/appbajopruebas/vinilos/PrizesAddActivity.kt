package com.appbajopruebas.vinilos

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.appbajopruebas.vinilos.databinding.ActivityPrizesAddBinding
class PrizesAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrizesAddBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el objeto de vinculación
        binding = ActivityPrizesAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar la ActionBar
        supportActionBar?.title = "Agregar Premiacion"


        // Obtener el fragmento de navegación desde esta Activity
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_prizes_add) as? NavHostFragment

        if (navHostFragment != null) {
            // Instanciar el NavController utilizando NavHostFragment
            navController = navHostFragment.navController
            // Asegurarse de que las acciones en la ActionBar se propaguen al NavController
            val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
            setupActionBarWithNavController(navController, appBarConfiguration)
        } else {
            // Manejar el caso en que navHostFragment es nulo (puede agregar un mensaje de registro o lanzar una excepción)
            Log.e("prizesAddActivity", "Error: navHostFragment es nulo.")
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
