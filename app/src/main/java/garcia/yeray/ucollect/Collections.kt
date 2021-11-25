package garcia.yeray.ucollect

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Collections {
    companion object {
        var objetos = mutableListOf<Objeto>()

        fun asignarObjetosLista() {
            objetos.clear()
            val bd = Firebase.firestore
            bd.collection("collection").whereNotEqualTo("userEmail", UserData.email!!).get().addOnSuccessListener { task ->
                if (task != null) {
                    val documents = task.documents
                    for (document in documents) {
                        bd.collection("collection")
                            .document(document.id).get().addOnSuccessListener {
                                val urlImg = it.get("urlImagen")
                                val nombre = it.get("nombreObjeto")
                                val tipo = it.get("tipoObjeto")
                                val precio = it.get("precioObjeto")
                                val intercambio = it.get("intercambioObjeto")
                                val userEmail = it.get("userEmail")
                                objetos.add(Objeto(urlImg.toString(),nombre.toString(),tipo.toString(),precio.toString(),intercambio.toString(),document.id,userEmail.toString()))
                            }
                    }
                }
            }
        }
    }
}