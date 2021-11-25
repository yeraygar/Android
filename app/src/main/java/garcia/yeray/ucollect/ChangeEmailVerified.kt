package garcia.yeray.ucollect

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import garcia.yeray.ucollect.databinding.ActivityChangeEmailVerifiedBinding

class ChangeEmailVerified : AppCompatActivity() {
    private lateinit var binding : ActivityChangeEmailVerifiedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeEmailVerifiedBinding.inflate(layoutInflater)
        binding.imageViewBack.setOnClickListener { onBackPressed()}
        binding.editTextEmailCambioCorreo.setText(UserData.email)
        binding.btnAceptar.setOnClickListener { chekConnectionToDb() }
        setContentView(binding.root)
    }

    fun chekConnectionToDb() {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if(networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected) {
           changeMail()
        }else{
            Toast.makeText(this, "Error de red", Toast.LENGTH_SHORT).show()
        }
    }

    fun changeMail() {
        if(binding.editTextEmailCambioCorreo.text.toString().isEmpty() || binding.editTextEmailCambioCorreo.text.toString().isBlank()) {
            binding.editTextEmailCambioCorreo.error = "Debe introducir un correo"
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.editTextEmailCambioCorreo.text.toString()).matches()){
            binding.editTextEmailCambioCorreo.error = "Correo no válido"
        } else {
            val progresDialog = ProgressDialog(this)
            progresDialog.setMessage("Actualizando...")
            progresDialog.setCancelable(false)
            progresDialog.show()
           UserData.auth!!.fetchSignInMethodsForEmail(binding.editTextEmailCambioCorreo.text.toString()).addOnCompleteListener { task ->
                    if (task.result?.signInMethods?.size != 0) {
                        binding.editTextEmailCambioCorreo.error = "Ese correo ya está en uso"
                    }else {
                       val credential = EmailAuthProvider.getCredential(UserData.email!!,UserData.userPassword)
                        UserData.user!!.reauthenticate(credential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                UserData.user!!.updateEmail(binding.editTextEmailCambioCorreo.text.toString()).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {

                                        //Obtener la imagen del usuario.
                                            //Si hay data, es decir, que el usuario no tenga la foto por defecto entra a la condición.
                                         if (UserData.data != null) {
                                             //Obtenemos la referencia de la imagen actual y la borramos.
                                             val reference = Firebase.storage.getReferenceFromUrl(UserData.urlImg!!)
                                             reference.delete()

                                             //Obtenemos el nuevo email del usuario.
                                             val userNewEmail = Firebase.auth.currentUser!!.email

                                            //Guardamos la imagen con el nuevo correo, de esta manera, el usuario solo tendrá 1 foto, y no se creara una foto nueva en el firestore si cambiamos el Correo.
                                             val newReference = Firebase.storage.reference.child("images/"+userNewEmail+"profilePic")
                                             newReference.putBytes(UserData.data!!).addOnSuccessListener {
                                                 newReference.downloadUrl.addOnCompleteListener { task ->
                                                     if (task.isSuccessful) {
                                                         UserData.urlImg = task.result.toString()
                                                     }
                                                 }
                                             }
                                         }
                                        val oldUser = UserData.email!!
                                        Toast.makeText(this, "Correo actualizado", Toast.LENGTH_SHORT).show()
                                        UserData.db?.collection("user")?.document(UserData.email!!)?.delete()
                                        UserData.saveUserData(UserData.nombre!!,UserData.apellidos!!,UserData.urlImg!!)
                                        //Comprobar todas las colecciones que tiene el usuario actual, y por cada coleccion cambiar el correo electronico anterior al actual
                                        val bd = Firebase.firestore
                                        bd.collection("collection").whereEqualTo("userEmail", oldUser).get().addOnSuccessListener { task ->
                                            if (task != null) {
                                                val documents = task.documents
                                                for (document in documents) {
                                                    bd.collection("collection")
                                                        .document(document.id)
                                                        .update("userEmail", UserData.user!!.email)
                                                }
                                            }
                                            UserCollections.asignarObjetosLista()
                                            if (progresDialog.isShowing) progresDialog.dismiss()
                                            onBackPressed()
                                        }
                                    }else {
                                        Toast.makeText(this, "Error al cambiar el correo", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }else {
                                Toast.makeText(this, "Error al cambiar el correo", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        }
    }
}