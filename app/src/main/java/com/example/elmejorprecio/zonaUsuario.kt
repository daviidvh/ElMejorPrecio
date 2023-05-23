package com.example.elmejorprecio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.elmejorprecio.databinding.ActivityZonaUsuarioBinding
import com.example.elmejorprecio.AdaptadorEditarEliminar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class zonaUsuario : AppCompatActivity() {

    private lateinit var recyclerView2: RecyclerView
    private lateinit var miAdaptador2: AdaptadorEditarEliminar
    private lateinit var listaProductos: MutableList<Producto>
    private lateinit var database: FirebaseFirestore
    private lateinit var usuario: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityZonaUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Inicializar variables
        recyclerView2 = findViewById(R.id.recyclerView2)
        listaProductos = mutableListOf()
        database = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance().currentUser!!

        // Configurar RecyclerView y adaptador
        miAdaptador2 = AdaptadorEditarEliminar(this, listaProductos)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView2.adapter = miAdaptador2

        // Obtener la lista de productos del usuario
        obtenerProductosUsuario()




        // Configura el SwipeRefreshLayout
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh2)
        swipeRefreshLayout.setOnRefreshListener {

            // Vuelve a cargar los productos
            obtenerProductosUsuario()

            // Cuando termines de actualizar los datos, debes llamar a setRefreshing(false) para
            // indicarle al SwipeRefreshLayout que deje de mostrar el indicador de progreso.
            swipeRefreshLayout.isRefreshing = false
        }

        //Volver a la pantalla de inicio
        binding.btnInicio.setOnClickListener(){
            val intent = Intent(this, muestraProductos::class.java)
            startActivity(intent)
        }

    }


    private fun obtenerProductosUsuario() {
        // Obtén una instancia de la base de datos de Firestore
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val emailUsuario = currentUser?.email
        // Accede a la subcolección de productos del usuario actual y ordénalos por fecha de manera descendente
        db.collection("usuarios").document(emailUsuario!!).collection("productos")
            .orderBy("fechaCreacion", Query.Direction.DESCENDING)
            .orderBy("precio", Query.Direction.DESCENDING)
            .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                listaProductos.clear() // Limpia la lista de productos antes de cargar los nuevos
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
                    val usuario = emailUsuario

                    val id =document.id

                    val producto = Producto(
                        nombreProducto!!,
                        precioProducto!!.toDouble(),
                        imagenProducto!!,
                        descripcionProducto!!,
                        supermercado!!,
                        usuario,
                        fecha!!,
                        id!!
                    )
                    listaProductos.add(producto)
                }

                // Notificar al adaptador del cambio en la lista de productos
                miAdaptador2.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("EditarProductosActivity", "Error al obtener los productos del usuario: ", exception)
            }
    }



    override fun onResume() {
        super.onResume()
        // Actualizar la lista de productos del usuario cuando se vuelva a la pantalla
        obtenerProductosUsuario()
    }
}
