package garcia.yeray.ucollect

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import garcia.yeray.ucollect.databinding.ActivityChangeEmailBinding
import garcia.yeray.ucollect.databinding.ActivityChangeEmailVerifiedBinding

class ChangeEmailVerified : AppCompatActivity() {
    private lateinit var binding : ActivityChangeEmailVerifiedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeEmailVerifiedBinding.inflate(layoutInflater)
        binding.imageViewBack.setOnClickListener { startActivity(Intent(this,ChangeEmail::class.java))}
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

    fun changeMail() :Boolean {
        var retorno = false
        if(binding.editTextEmailCambioCorreo.text.toString().isEmpty() || binding.editTextEmailCambioCorreo.text.toString().isBlank()) {
            binding.editTextEmailCambioCorreo.error = "Debe introducir un correo"
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.editTextEmailCambioCorreo.text.toString()).matches()){
            binding.editTextEmailCambioCorreo.error = "Correo no válido"
        } else {
           UserData.auth!!.fetchSignInMethodsForEmail(binding.editTextEmailCambioCorreo.text.toString()).addOnCompleteListener { task ->
                    if (task.result?.signInMethods?.size != 0) {
                        binding.editTextEmailCambioCorreo.error = "Ese correo ya está en uso"
                    }else {
                       val credential = EmailAuthProvider.getCredential(UserData.email!!,UserData.userPassword)
                        UserData.user!!.reauthenticate(credential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                UserData.user!!.updateEmail(binding.editTextEmailCambioCorreo.text.toString()).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Correo actualizado", Toast.LENGTH_SHORT).show()
                                        UserData.db?.collection("user")?.document(UserData.email!!)?.delete()
                                        UserData.saveUserData(UserData.nombre!!,UserData.apellidos!!,UserData.urlImg!!)
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
        return retorno
    }
}