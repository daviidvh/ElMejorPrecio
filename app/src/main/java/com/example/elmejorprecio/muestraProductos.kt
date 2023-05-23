package com.example.elmejorprecio


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.elmejorprecio.databinding.ActivityPrincipalBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class muestraProductos : AppCompatActivity() {
    private lateinit var binding: ActivityPrincipalBinding
    private var listaProductos = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var emailUsuario= intent.getStringExtra("emailUsuario")

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        cargarProductos()

        binding.agregar.setOnClickListener(){
            val intent = Intent(this, agregarProductos::class.java)
            intent.putExtra("emailUsuario",emailUsuario)
            startActivity(intent)
        }

        binding.editar.setOnClickListener(){
            val intent = Intent(this, zonaUsuario::class.java)
            intent.putExtra("emailUsuario",emailUsuario)
            startActivity(intent)
        }

        // Configura el SwipeRefreshLayout
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefreshLayout.setOnRefreshListener {
            // Coloca aquí el código que deseas ejecutar cuando se realiza el Swipe to Refresh

            // Vuelve a cargar los productos
            cargarProductos()

            // Cuando termines de actualizar los datos, debes llamar a setRefreshing(false) para
            // indicarle al SwipeRefreshLayout que deje de mostrar el indicador de progreso.
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun cargarProductos() {
        // Obtén una instancia de la base de datos de Firestore
        val db = FirebaseFirestore.getInstance()

        // Accede a la colección de productos de todos los usuarios y ordénalos por fecha de manera descendente
        db.collectionGroup("productos")
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

                    // Obtiene el email del usuario a partir de la ruta del documento
                    val usuario = document.reference.parent.parent!!.id

                    val id=document.id
                    //Creamos la instancia al Producta y tratamos los nulos
                    val product = Producto(
                        nombreProducto!!,
                        precioProducto!!.toDouble(),
                        imagenProducto!!,
                        descripcionProducto!!,
                        supermercado!!,
                        usuario!!,
                        fecha!!,
                        id!!
                    )
                    listaProductos.add(product)
                }

                // Crea un adaptador para el RecyclerView y usa la lista de productos
                val adapter = MiAdaptador(this, listaProductos)
                binding.recyclerView.adapter = adapter
            }
    }
}
