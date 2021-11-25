package garcia.yeray.ucollect

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import garcia.yeray.ucollect.databinding.ActivityFortgotPasswordBinding

class FortgotPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityFortgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFortgotPasswordBinding.inflate(layoutInflater)
        binding.imageViewAtras.setOnClickListener{ onBackPressed() }
        binding.buttonEnviarForgot.setOnClickListener { enviarEmail(binding.editTextEmailForgot.text.toString()) }
        auth = Firebase.auth
        setContentView(binding.root)
    }

    private fun enviarEmail(email :String){
        if (chekConnectionToDb()) {
            if (email.isEmpty() || email.isBlank()) {
                binding.editTextEmailForgot.error = "Debe introducir un email"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextEmailForgot.error = "Debe introducir un correo válido"
            } else {
                val progresDialog = ProgressDialog(this)
                progresDialog.setMessage("Actualizando...")
                progresDialog.setCancelable(false)
                progresDialog.show()
                val auth = Firebase.auth
                auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
                    if (task.result?.signInMethods?.size != 0) {
                        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (progresDialog.isShowing) progresDialog.dismiss()
                                Toast.makeText(this,"Revise su Email para cambiar la contraseña",Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        binding.editTextEmailForgot.error = "No existe ningún correo con esa cuenta"
                    }
                }
            }
        }else{
            Toast.makeText(this, "Error de red", Toast.LENGTH_SHORT).show()
        }
    }

    fun chekConnectionToDb() : Boolean{
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if(networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected) {
            return true
        }else{
            return false
        }
    }
}