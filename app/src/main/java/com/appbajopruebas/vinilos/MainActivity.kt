package com.appbajopruebas.vinilos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import android.widget.TextView
import brokers.VolleyBroker
import com.android.volley.Response

class MainActivity : AppCompatActivity() {
    lateinit var volleyBroker: VolleyBroker

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.layout_menu, menu)
        supportActionBar!!.title = "Volley"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                // Create an intent with a destination of the other Activity
                val intent = Intent(this, colleccionistaActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        volleyBroker = VolleyBroker(this.applicationContext)



        //listener btn_Vinilos
        val minicard1 = findViewById<RelativeLayout>(R.id.minicard1)
        val getResultTextView : TextView = findViewById(R.id.get_result_text)
        minicard1.setOnClickListener {
            val intent = Intent(this, VinilosActivity::class.java)
            startActivity(intent)

        /*
            volleyBroker.instance.add(VolleyBroker.getRequest("collectors",
                Response.Listener<String> { response ->
                    // Display the first 500 characters of the response string.
                    getResultTextView.text = "Response is: ${response}"
                },
                Response.ErrorListener {
                    Log.d("TAG", it.toString())
                    getResultTextView.text = "That didn't work!"
                }
            ))*/

        }

        //listener btn_Artista
        val minicard2 = findViewById<RelativeLayout>(R.id.minicard2)
        minicard2.setOnClickListener {
            val intent = Intent(this, ListArtistasActivity::class.java)
            startActivity(intent)
        }

        //listener btn_Collecionista
        val minicard3 = findViewById<RelativeLayout>(R.id.minicard3)
        minicard3.setOnClickListener {
            val intent = Intent(this, colleccionistaActivity::class.java)
            startActivity(intent)
        }


    }
}