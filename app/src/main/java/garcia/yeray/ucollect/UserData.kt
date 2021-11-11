package garcia.yeray.ucollect

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder

class UserData {

    companion object{
        var auth :FirebaseAuth? = null
        var user : FirebaseUser? = null
        var email : String? = null
        var db : FirebaseFirestore? = null
        var nombre : String? = null
        var apellidos : String? = null


        fun saveUserData(name:String, ape:String) {
            auth = Firebase.auth
            user = auth?.currentUser
            email = user?.email
            nombre = name
            apellidos = ape
            db = FirebaseFirestore.getInstance()
            db?.collection("user")?.document(email!!)?.set(hashMapOf("nombre" to nombre,"apellidos" to apellidos))
        }

        fun restoreUserData() {
            auth = Firebase.auth
            user = auth?.currentUser
            email = user?.email
            db = FirebaseFirestore.getInstance()
            db?.collection("user")?.document(email.toString())?.get()?.addOnSuccessListener {
                nombre = it.get("nombre").toString()
                apellidos = it.get("apellidos").toString()
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