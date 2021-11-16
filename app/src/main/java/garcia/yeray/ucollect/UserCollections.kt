package garcia.yeray.ucollect

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UserCollections {

    companion object{

        var ImageDataFromCollection : Uri? = null

        fun uploadToFirebase(imagenObjeto : Uri, nombreObjeto:String, tipoObjeto : String, precioObjeto: Double, intercambioObjeto : Boolean) {
            val db = FirebaseFirestore.getInstance()
            val coleccion = db.collection("collection").document()
            coleccion.set(
                hashMapOf(
                    "urlImagen" to "urlImagen",
                    "nombreObjeto" to nombreObjeto,
                    "tipoObjeto" to tipoObjeto,
                    "precioObjeto" to precioObjeto,
                    "intercambioObjeto" to intercambioObjeto
                )
            ).addOnSuccessListener {
                val referenceToImage = Firebase.storage.reference.child("CollectionImages/"+coleccion.id)
                val addImage = referenceToImage.putFile(ImageDataFromCollection!!)
                addImage.addOnFailureListener{

                }.addOnSuccessListener { task ->
                   referenceToImage.downloadUrl.addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           val url = task.result.toString()
                           coleccion.update("urlImagen",url)
                       }
                   }
                }
            }
        }
    }
}