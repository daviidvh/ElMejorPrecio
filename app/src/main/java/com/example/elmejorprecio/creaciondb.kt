package com.example.elmejorprecio

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
class creaciondb {

    private val db = FirebaseFirestore.getInstance()

    fun crearDatosUsuario(email: String) {
        // Crear usuario
        val usuario = hashMapOf(
            "email" to email
        )

        db.collection("usuarios").document(email).set(usuario)
    }

    fun crearDatosProductos(email: String, nombre: String, precio: Double, descripcion: String, supermecado: String, imagenUrl: String) {
        // Crear producto
        val producto = hashMapOf(
            "nombre" to nombre,
            "descripcion" to descripcion,
            "precio" to precio,
            "supermecado" to supermecado,
            "imagenUrl" to imagenUrl,
            "fechaCreacion" to FieldValue.serverTimestamp()
        )
        db.collection("usuarios").document(email).collection("productos").add(producto)
    }
}
