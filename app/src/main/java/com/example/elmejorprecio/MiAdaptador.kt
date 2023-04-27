package com.example.elmejorprecio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class MiAdaptador(private val context: Context, private val listaProductos: List<Producto>) :
    RecyclerView.Adapter<MiAdaptador.ViewHolder>() {

    // Clase ViewHolder que se encarga de mantener las referencias de los elementos de la vista
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val nameTextView: TextView = view.findViewById(R.id.txt_nombre)
        val priceTextView: TextView = view.findViewById(R.id.txt_precio)
        val descriptionTextView: TextView = view.findViewById(R.id.txt_descripcion)
        val supermarketTextView: TextView = view.findViewById(R.id.txt_supermercado)
        val userTextView: TextView = view.findViewById(R.id.txt_Usuario)
        val fechaCreacion: TextView =view.findViewById(R.id.fechaCreacion)
    }

    // Este método se llama cuando se crea el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflar la vista del elemento de la lista usando el layout lista_productos
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_productos, parent, false)
        // Crear y retornar el ViewHolder
        return ViewHolder(view)
    }

    // Este método se llama cuando se debe actualizar el contenido del ViewHolder con el elemento en la posición dada
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Obtener el producto en la posición dada
        val product = listaProductos[position]
        // Establecer el nombre del producto en el TextView correspondiente
        holder.nameTextView.text = product.nombreProducto
        // Establecer el precio del producto en el TextView correspondiente
        holder.priceTextView.text = "${product.precioProducto}€"
        // Establecer la descripción del producto en el TextView correspondiente
        holder.descriptionTextView.text = product.descripcionProducto
        // Establecer el supermercado del producto en el TextView correspondiente
        holder.supermarketTextView.text = product.supermercado
        // Establecer el usuario que publicó el producto en el TextView correspondiente
        holder.userTextView.text = product.usuario
        // Formato de fecha que deseas mostrar
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.fechaCreacion.text = dateFormat.format(product.fechaCreacion?.toDate())
        // Cargar la imagen del producto en el ImageView usando Glide
        Glide.with(context)
            .load(product.imagenProducto)
            .placeholder(R.drawable.noimagen) // Establecer la imagen por defecto mientras se carga la imagen del producto
            .into(holder.imageView)
    }

    // Este método retorna la cantidad de elementos en la lista
    override fun getItemCount(): Int = listaProductos.size
}

