package garcia.yeray.ucollect

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class ObjetoAdapterCollections(private val objeto : List<Objeto>): RecyclerView.Adapter<ObjetoAdapterCollections.ObjetoHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjetoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ObjetoHolder(layoutInflater.inflate(R.layout.vista_objetos_colecciones,parent,false))
    }

    override fun onBindViewHolder(holder: ObjetoHolder, position: Int) {
        holder.render(objeto[position])
    }

    override fun getItemCount(): Int = objeto.size

    class ObjetoHolder(val view: View):RecyclerView.ViewHolder(view) {
        fun render(objeto : Objeto) {
            val imagen = view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.imagenObjeto)
            val titulo = view.findViewById<TextView>(R.id.tituloObjeto)
            val tipo = view.findViewById<TextView>(R.id.tipoObjeto)
            val precio = view.findViewById<TextView>(R.id.precioObjeto)
            val emailUser = view.findViewById<TextView>(R.id.usuarioEmail)
            titulo.text = objeto.nombre
            Glide.with(view).load(objeto.UrlImg).into(imagen)
            tipo.text = objeto.tipo
            precio.text = objeto.precio
            emailUser.text = objeto.User
            view.setOnClickListener {
                val intent = Intent(view.context,verObjetos::class.java)
                intent.putExtra("urlImg",objeto.UrlImg)
                intent.putExtra("nombre",objeto.nombre)
                intent.putExtra("tipo",objeto.tipo)
                intent.putExtra("precio",objeto.precio)
                intent.putExtra("intercambio",objeto.intercambio)
                intent.putExtra("id",objeto.idColeccion)
                intent.putExtra("indexUserCollection",UserCollections.objetos.indexOf(objeto))
                intent.putExtra("userEmail",objeto.User)
                view.context.startActivity(intent)
            }
        }
    }

}