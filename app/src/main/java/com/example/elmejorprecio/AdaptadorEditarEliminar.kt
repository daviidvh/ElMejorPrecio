package com.example.elmejorprecio

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale
// ...importaciones de bibliotecas...

class AdaptadorEditarEliminar(private val context: Context, private val listaProductos: List<Producto>) :
    RecyclerView.Adapter<AdaptadorEditarEliminar.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val nameTextView: TextView = view.findViewById(R.id.txt_nombre)
        val priceTextView: TextView = view.findViewById(R.id.txt_precio)
        val descriptionTextView: TextView = view.findViewById(R.id.txt_descripcion)
        val supermarketTextView: TextView = view.findViewById(R.id.txt_supermercado)
        val userTextView: TextView = view.findViewById(R.id.txt_Usuario)
        val fechaCreacion: TextView = view.findViewById(R.id.fechaCreacion)
        val btnEliminar: ImageView = view.findViewById(R.id.eliminar_button)
        val btnEditar: ImageView = view.findViewById(R.id.editar_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.edicion_productos, parent, false)
        return ViewHolder(view)
    }

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
            // Acción al hacer clic en el botón de eliminar
        }

        holder.btnEditar.setOnClickListener {
            // Acción al hacer clic en el botón de editar
        }

        // Establecer las imágenes de los botones eliminar y editar
        holder.btnEliminar.setImageResource(R.drawable.eliminar)
        holder.btnEditar.setImageResource(R.drawable.editar)

        // Establecer la imagen del producto
        Glide.with(context)
            .load(product.imagenProducto)
            .placeholder(R.drawable.noimagen)
            .into(holder.imageView)

        Log.d("RecyclerView", "Se actualizó el ViewHolder para RecyclerView: ${holder.itemView.parent.toString()}")
    }

    override fun getItemCount(): Int = listaProductos.size
}
