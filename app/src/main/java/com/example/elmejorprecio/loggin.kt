package com.examp0le.elmejorprecio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.elmejorprecio.databinding.ActivityLogginBinding

class loggin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLogginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}