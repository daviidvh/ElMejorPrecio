package com.example.elmejorprecio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.elmejorprecio.databinding.ActivityAgregarProductosBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage

class agregarProductos : AppCompatActivity() {
    private val REQUEST_SELECT_IMAGE = 2
    private var imagenUri: android.net.Uri? = null // variable para almacenar la URI de la imagen
    private var imagenUrl: String? = null // variable para almacenar la URL de descarga de la imagen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAgregarProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var emailUsuario= intent.getStringExtra("emailUsuario")

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)

        binding.btnSubirImg.setOnClickListener(){
            // Crear un intent para abrir la galería de imágenes
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            // Iniciar la actividad para seleccionar una imagen
            startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), REQUEST_SELECT_IMAGE)
        }

        binding.btnSubir.setOnClickListener(){
            if (!binding.txtNombre.text.isNullOrEmpty() && !binding.txtPrecio.text.isNullOrEmpty() && !binding.txtDescripcion.text.isNullOrEmpty() && !binding.txtSupermercado.text.isNullOrEmpty() && imagenUri != null) {
                // Creamos una referencia en Firebase Storage con el nombre de la imagen
                val storageRef = FirebaseStorage.getInstance().reference.child("images/${imagenUri?.lastPathSegment}")
                // Subimos la imagen a Firebase Storage y obtenemos su URL de descarga
                Toast.makeText(this, "Subiendo producto...", Toast.LENGTH_SHORT).show()
                storageRef.putFile(imagenUri!!).continueWithTask { task ->
                    if (!task.isSuccessful) {task.exception?.let {
                                throw it
                            }
                        }
                        storageRef.downloadUrl
                    }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                            imagenUrl = task.result.toString()
                            // Una vez se ha subido la imagen, creamos los datos del usuario y del producto
                            val datos = creaciondb()
                            datos.crearDatosUsuario(emailUsuario.toString())
                            datos.crearDatosProductos(emailUsuario.toString(), binding.txtNombre.text.toString(), binding.txtPrecio.text.toString().toDouble(), binding.txtDescripcion.text.toString(), binding.txtSupermercado.text.toString(), imagenUrl!!)
                        val intent = Intent(this, muestraProductos::class.java)
                            Toast.makeText(this, "Producto añadido correctamente", Toast.LENGTH_SHORT).show()
                        intent.putExtra("emailUsuario",emailUsuario)
                        startActivity(intent)
                        } else {
                            Toast.makeText(this, "Error al subir la imagen: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

            } else {
                Toast.makeText(this, "Por favor ingresa todos los datos y sube una imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Comprobamos que la actividad que ha finalizado es la de selección de imagen
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            // Obtenemos la URI de la imagen seleccionada
            imagenUri = data.data
        }
    }
}
