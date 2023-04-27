package com.example.elmejorprecio

import com.google.firebase.Timestamp

data class Producto(
    val nombreProducto: String = "",
    val precioProducto: Double,
    val imagenProducto: String = "",
    val descripcionProducto: String = "",
    val supermercado: String = "",
    val usuario: String = "",
    val fechaCreacion: Timestamp?
)
