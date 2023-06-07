package com.example.elmejorprecio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.elmejorprecio.databinding.ActivityMicuentaBinding
import com.example.elmejorprecio.databinding.ActivityZonaUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class micuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMicuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Actualizar los datos del producto en la base de datos
        val db = FirebaseFirestore.getInstance()
        //Cogemos el email de la sesion
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val emailUsuario = currentUser?.email

        binding.txtUsuario.text = "Usuario:" + emailUsuario
        // Contar el total de productos del usuario
        db.collection("usuarios")
            .document(emailUsuario!!)
            .collection("productos")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val totalProductos = querySnapshot.size()
                binding.txtTotalProductos.text = "Total de productos:$totalProductos"
            }
        binding.btnEliminarCuenta.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Todos los datos se eliminarán de forma permanente.")
            builder.setPositiveButton("Eliminar") { dialog, which ->
                // Eliminar productos del usuario en Cloud Firestore
                db.collection("usuarios")
                    .document(emailUsuario)
                    .collection("productos")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        querySnapshot.documents.forEach { document ->
                            document.reference.delete()
                        }
                        // Eliminar datos del usuario en Cloud Firestore
                        db.collection("usuarios")
                            .document(emailUsuario)
                            .delete()
                            .addOnSuccessListener {
                                // Eliminar datos del usuario en FirebaseAuth
                                currentUser.delete()
                                    .addOnSuccessListener {
                                        // Cerrar sesión y redirigir al inicio de sesión
                                        auth.signOut()
                                        startActivity(Intent(this, login::class.java))
                                        finish()
                                    }
                            }
                    }
            }
            builder.setNegativeButton("Cancelar", null)
            val dialog = builder.create()
            dialog.show()
        }
    }
}