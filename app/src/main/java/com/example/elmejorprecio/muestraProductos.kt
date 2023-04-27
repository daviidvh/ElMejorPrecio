package com.example.elmejorprecio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elmejorprecio.databinding.ActivityPrincipalBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class muestraProductos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var emailUsuario= intent.getStringExtra("emailUsuario")

        binding.recyclerView.layoutManager = LinearLayoutManager(this)


        // Obtén una instancia de la base de datos de Firestore
        val db = FirebaseFirestore.getInstance()

        // Accede a la colección de productos de todos los usuarios y ordénalos por fecha de manera descendente
        db.collectionGroup("productos")
            .orderBy("fechaCreacion", Query.Direction.DESCENDING)
            .orderBy("precio", Query.Direction.DESCENDING)
            .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val productList = mutableListOf<Producto>()
                    for (document in result) {
                        // Recupera los datos de cada documento y crea un objeto de productos
                        val nombreProducto = document.getString("nombre")
                        val precioProducto = document.getDouble("precio")
                        val imagenProducto = document.getString("imagenUrl")
                        val descripcionProducto = document.getString("descripcion")
                        val supermercado = document.getString("supermecado")
                        //Tenemos dos fechas para tratar los nulos
                        val fechaCreacion = document.getTimestamp("fechaCreacion")
                        val fecha = fechaCreacion ?: Timestamp.now()
                        val usuario =
                            document.reference.parent.parent!!.id // Obtiene el email del usuario a partir de la ruta del documento

                        val product = Producto(
                            nombreProducto!!,
                            precioProducto!!.toDouble(),
                            imagenProducto!!,
                            descripcionProducto!!,
                            supermercado!!,
                            usuario!!,
                            fecha!!
                        )
                        productList.add(product)
                    }

                    // Crea un adaptador para el RecyclerView y usa la lista de productos
                    val adapter = MiAdaptador(this, productList)
                    binding.recyclerView.adapter = adapter

            }

        binding.agregar.setOnClickListener(){
            val intent = Intent(this, agregarProductos::class.java)
            intent.putExtra("emailUsuario",emailUsuario)
            startActivity(intent)
        }
    }
}

