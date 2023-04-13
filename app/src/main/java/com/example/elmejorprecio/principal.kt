package com.example.elmejorprecio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.elmejorprecio.databinding.ActivityPrincipalBinding

class principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var emailUsuario= intent.getStringExtra("emailUsuario")

        binding.agregar.setOnClickListener(){
            val intent = Intent(this, agregarProductos::class.java)
            intent.putExtra("emailUsuario",emailUsuario)
            startActivity(intent)
        }
    }
}