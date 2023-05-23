package com.example.elmejorprecio
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.elmejorprecio.Producto
import com.example.elmejorprecio.R
import com.example.elmejorprecio.editarProducto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale



class AdaptadorEditarEliminar(private val context: Context, private val listaProductos: MutableList<Producto>)
    : RecyclerView.Adapter<AdaptadorEditarEliminar.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val nameTextView: TextView = view.findViewById(R.id.txt_nombre)
        val priceTextView: TextView = view.findViewById(R.id.txt_precio)
        val descriptionTextView: TextView = view.findViewById(R.id.txt_descripcion)
        val supermarketTextView: TextView = view.findViewById(R.id.txt_supermercado)
        val userTextView: TextView = view.findViewById(R.id.txt_Usuario)
        val fechaCreacion: TextView = view.findViewById(R.id.fechaCreacion)
        val btnEliminar: Button = view.findViewById(R.id.eliminar_button)
        val btnEditar: Button = view.findViewById(R.id.editar_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.edicion_productos, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = listaProductos[position]
        holder.nameTextView.text = product.nombreProducto
        holder.priceTextView.text = "${product.precioProducto}€"
        holder.descriptionTextView.text = product.descripcionProducto
        holder.supermarketTextView.text = product.supermercado
        holder.userTextView.text = product.usuario
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.fechaCreacion.text = dateFormat.format(product.fechaCreacion?.toDate())


        holder.btnEliminar.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Eliminar Producto")
            alertDialog.setMessage("¿Estás seguro de que deseas eliminar este producto?")

            alertDialog.setPositiveButton("Sí") { dialog, which ->
                val auth = FirebaseAuth.getInstance()

                // Obtener el email del usuario actualmente autenticado
                val currentUser = auth.currentUser
                val userEmail = currentUser?.email

                val productId = product.id
                // Obtener la instancia de FirebaseFirestore
                val db = FirebaseFirestore.getInstance()

                    // Realizar la eliminación del producto
                    db.collection("usuarios").document(userEmail!!).collection("productos").document(productId).delete()
                        .addOnSuccessListener {
                            // Eliminar el producto de la lista
                            listaProductos.remove(product)
                            notifyItemRemoved(holder.adapterPosition)
                            notifyItemRangeChanged(holder.adapterPosition, listaProductos.size)

                            Toast.makeText(context, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(context, "Error al eliminar el producto: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
            }

            alertDialog.setNegativeButton("No") { dialog, which ->
                Toast.makeText(context, "Producto NO eliminado", Toast.LENGTH_SHORT).show()

            }

            alertDialog.show()
        }






        holder.btnEditar.setOnClickListener {
            val intent = Intent(context, editarProducto::class.java)
            intent.putExtra("productoId", product.id)
            intent.putExtra("nombreProducto", product.nombreProducto)
            intent.putExtra("precioProducto", product.precioProducto)
            intent.putExtra("imagenProducto", product.imagenProducto)
            intent.putExtra("descripcionProducto", product.descripcionProducto)
            intent.putExtra("supermercado", product.supermercado)
            context.startActivity(intent)
        }



        Glide.with(context)
            .load(product.imagenProducto)
            .placeholder(R.drawable.noimagen)
            .into(holder.imageView)

        Log.d(
            "RecyclerView",
            "Se actualizó el ViewHolder para RecyclerView: ${holder.itemView.parent.toString()}"
        )
        Log.d(TAG, "onBindViewHolder called for position: $position")

    }


    override fun getItemCount(): Int = listaProductos.size

}