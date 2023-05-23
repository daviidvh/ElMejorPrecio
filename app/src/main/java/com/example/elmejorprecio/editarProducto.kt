package com.example.elmejorprecio

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.example.elmejorprecio.databinding.ActivityEditarProductoBinding
import com.google.firebase.auth.FirebaseAuth

class editarProducto : AppCompatActivity() {
    private lateinit var binding: ActivityEditarProductoBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Cogemos los datos del producto seleccionado
        val productoId = intent.getStringExtra("productoId")
        val nombreProducto = intent.getStringExtra("nombreProducto")
        val precioProducto = intent.getDoubleExtra("precioProducto", 0.0)
        val descripcionProducto = intent.getStringExtra("descripcionProducto")
        val supermercado = intent.getStringExtra("supermercado")

        // Establecer los valores en los campos correspondientes
        binding.txtNombre.setText(nombreProducto)
        binding.txtPrecio.setText(precioProducto.toString())
        binding.txtDescripcion.setText(descripcionProducto)
        binding.txtSupermercado.setText(supermercado)

        // Implementar lógica para guardar los cambios
        binding.btnActualizar.setOnClickListener {
            //Pasamos a texto lo que hay escrito en los datos del producto
            val nuevoNombre = binding.txtNombre.text.toString()
            val nuevoPrecio = binding.txtPrecio.text.toString().toDouble()
            val nuevaDescripcion = binding.txtDescripcion.text.toString()
            val nuevoSupermercado = binding.txtSupermercado.text.toString()

            // Actualizar los datos del producto en la base de datos
            val db = FirebaseFirestore.getInstance()
            //Cogemos el email de la sesion
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val emailUsuario = currentUser?.email


                // Realizar la eliminación del producto
                db.collection("usuarios").document(emailUsuario!!).collection("productos").document(productoId!!).
                update(
                "nombre", nuevoNombre,
                "precio", nuevoPrecio,
                "descripcion", nuevaDescripcion,
                "supermecado", nuevoSupermercado
                )
                .addOnSuccessListener {
                    Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error al actualizar el producto: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
