package com.appbajopruebas.vinilos

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import brokers.RetrofitBroker

class colleccionistaActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colleccionista)
        // Habilita el botón de retroceso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lista Collecion"

        setContentView(R.layout.activity_colleccionista)

        val getButton: Button = findViewById(R.id.fetch_button_2)
        val getResultTextView: TextView = findViewById(R.id.get_result_text_2)
        getButton.setOnClickListener {
            RetrofitBroker.getRequest(onResponse = {
                getResultTextView.text = it
            }, onFailure = {
                getResultTextView.text = it
            })

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.layout_menu, menu)
        supportActionBar!!.title = "Volley"
        return true
    }

    // Esta función se llama cuando se presiona el botón de retroceso en la barra de acción
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                // Create an intent with a destination of the other Activity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




}