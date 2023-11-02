package com.appbajopruebas.vinilos

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //listener btn_Vinilos
        val minicard1 = findViewById<RelativeLayout>(R.id.minicard1)
        minicard1.setOnClickListener {
            val intent = Intent(this, VinilosActivity::class.java)
            startActivity(intent)
        }

        //listener btn_Artista
        val minicard2 = findViewById<RelativeLayout>(R.id.minicard2)
        minicard2.setOnClickListener {
            val intent = Intent(this, artistaActivity::class.java)
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