package garcia.yeray.ucollect

import android.graphics.Bitmap
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UserData {

    companion object{
        private val storage = Firebase.storage
        var auth :FirebaseAuth? = null
        var user : FirebaseUser? = null
        var email : String? = null
        var db : FirebaseFirestore? = null
        var nombre : String? = null
        var apellidos : String? = null
        var urlImg : String? = null
        var data : ByteArray? = null
        var bitmapImg : Bitmap? = null
        lateinit var userPassword : String



        fun saveUserData(name:String, ape:String ,url:String) {
            auth = Firebase.auth
            user = auth?.currentUser
            email = user?.email
            nombre = name
            apellidos = ape
            urlImg = url
            db = FirebaseFirestore.getInstance()
            db?.collection("user")?.document(email!!)?.set(hashMapOf("nombre" to nombre,"apellidos" to apellidos,"urlImagen" to urlImg))
            urlImg = url
            //Guardar imagen default
        }

        fun changeName(name:String) {
           nombre = name
            db?.collection("user")?.document(email!!)?.update("nombre", nombre)
        }

        fun changeLastName(ape:String){
            apellidos = ape
            db?.collection("user")?.document(email!!)?.update("apellidos", apellidos)
        }

        fun changeMail(mail:String){
            val credencial = EmailAuthProvider.getCredential(email!!, userPassword)
            user!!.reauthenticate(credencial).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = auth!!.currentUser
                    user!!.updateEmail(mail).addOnCompleteListener {
                        if (task.isSuccessful) {
                            db?.collection("users")?.document(email!!)?.delete()?.addOnCompleteListener {
                                email = user!!.email
                                saveUserData(nombre!!, apellidos!!, urlImg!!)
                            }
                            email = mail

                        }
                    }
                }
            }
        }

        fun uploadImage(data : ByteArray) {
            val storageRef = storage.reference
            val perfilImagesRef = storageRef.child("images/" + email + "profilePic")
            val uploadTask = perfilImagesRef.putBytes(data)
            uploadTask.addOnFailureListener {
                //Controlamos los fallos
            }.addOnSuccessListener { taskSnapshot ->
                perfilImagesRef.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        urlImg = task.result.toString()
                        db?.collection("user")?.document(email!!)?.update("urlImagen", urlImg)
                    }
                }
            }
        }

        fun restoreUserData() {
            auth = Firebase.auth
            user = auth?.currentUser
            email = user?.email
            db = FirebaseFirestore.getInstance()
            if (nombre == null) {
                db?.collection("user")?.document(email.toString())?.get()?.addOnSuccessListener {
                    nombre = it.get("nombre").toString()
                    apellidos = it.get("apellidos").toString()
                    urlImg = it.get("urlImagen").toString()
                }
            }
        }

        fun resetData() {
            auth = null
            user = null
            email = null
            db = null
            nombre = null
            apellidos = null
        }
    }
}