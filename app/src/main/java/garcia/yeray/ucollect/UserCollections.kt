package garcia.yeray.ucollect

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UserCollections {
    companion object{
        private val tag = "UserCollection"
         var objetos = mutableListOf<Objeto>()
         //var reservas = mutableListOf<Objeto>()
         var ImageDataFromCollection : Uri? = null


        fun asignarObjetosLista() {
            objetos.clear()
            val bd = Firebase.firestore
             val coleccionesUsuario = bd.collection("collection").whereEqualTo("userEmail", UserData.email!!).get().addOnSuccessListener { task ->
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
                                objetos!!.add(Objeto(urlImg.toString(),nombre.toString(),tipo.toString(),precio.toString(),intercambio.toString(),document.id,userEmail.toString()))
                                Log.d("objetos_UserCollections", objetos.size.toString())
                            }
                    }
                }
            }
        }
    }
}