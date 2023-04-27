package com.example.elmejorprecio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.elmejorprecio.login
import com.example.elmejorprecio.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth


class registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrarRegistro.setOnClickListener() {
            if (binding.edtEmailRegistro.text.isNotEmpty() && binding.edtPasswordRegistro.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.edtEmailRegistro.text.toString(),
                    binding.edtPasswordRegistro.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Se ha registrado al usuario", Toast.LENGTH_SHORT).show()
                        val intent= Intent(this, login::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error al autentificar el usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}