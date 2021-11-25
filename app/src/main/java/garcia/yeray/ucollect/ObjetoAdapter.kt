package garcia.yeray.ucollect

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class ObjetoAdapter(private val objeto : List<Objeto>):RecyclerView.Adapter<ObjetoAdapter.ObjetoHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjetoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ObjetoHolder(layoutInflater.inflate(R.layout.vista_objeto_coleccion,parent,false))
    }

    override fun onBindViewHolder(holder: ObjetoHolder, position: Int) {
        holder.render(objeto[position])
    }

    override fun getItemCount(): Int = objeto.size

    class ObjetoHolder(val view: View):RecyclerView.ViewHolder(view) {
        fun render(objeto : Objeto) {
            val imagen = view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.imagenObjeto)
            val titulo = view.findViewById<TextView>(R.id.nombreObjeto)
            titulo.text = objeto.nombre
            Glide.with(view).load(objeto.UrlImg).into(imagen)
            view.setOnClickListener {
                val intent = Intent(view.context,VerObjetoUsuario::class.java)
                intent.putExtra("urlImg",objeto.UrlImg)
                intent.putExtra("nombre",objeto.nombre)
                intent.putExtra("tipo",objeto.tipo)
                intent.putExtra("precio",objeto.precio)
                intent.putExtra("intercambio",objeto.intercambio)
                intent.putExtra("id",objeto.idColeccion)
                intent.putExtra("indexUserCollection",UserCollections.objetos.indexOf(objeto))
                view.context.startActivity(intent)
            }
        }
    }

}