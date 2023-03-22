package com.example.elmejorprecio

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.elmejorprecio.databinding.ActivityPrincipalBinding
import com.example.elmejorprecio.databinding.ActivityRegistroBinding

class principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}