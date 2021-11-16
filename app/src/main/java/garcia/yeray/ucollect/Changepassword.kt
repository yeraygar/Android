package garcia.yeray.ucollect

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import garcia.yeray.ucollect.databinding.ActivityChangepasswordBinding

class Changepassword : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityChangepasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityChangepasswordBinding.inflate(layoutInflater)
        binding.ImagenVerPassword.setOnClickListener { changeVisibilityMode(binding.ImagenVerPassword,binding.EditTextOldPassword) }
        binding.ImagenverNuevaPass.setOnClickListener { changeVisibilityMode(binding.ImagenverNuevaPass,binding.EditTextnewPassword) }
        binding.btnAceptar.setOnClickListener { checkValues(binding.EditTextOldPassword.text.toString(),binding.EditTextnewPassword.text.toString()) }
        binding.imageViewBack.setOnClickListener { startActivity(Intent(this,EditProfile::class.java)) }
        binding.EditTextOldPassword.doAfterTextChanged { cambiarEstadoImageButton(binding.ImagenVerPassword,binding.EditTextOldPassword.text.toString())}
        binding.EditTextnewPassword.doAfterTextChanged { cambiarEstadoImageButton(binding.ImagenverNuevaPass,binding.EditTextnewPassword.text.toString()) }
        setContentView(binding.root)
    }

    private fun changeVisibilityMode(imagen : ImageView, editText : EditText) {
        if(imagen.tag.toString() == "ver_pass") {
            imagen.tag = "ocultar_pass"

            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imagen.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }else {
            imagen.tag = "ver_pass"
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            imagen.setImageResource(R.drawable.ic_baseline_visibility_24)
        }
    }

    private fun checkValues(password: String, new_password:String) {
        if (chekConnectionToDb()) {
            if (password.isNotEmpty()) {
                val user = auth.currentUser
                var credential = EmailAuthProvider.getCredential(user?.email!!, password)
                user.reauthenticate(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (new_password.isNotEmpty() && new_password.length >= 6) {
                            credential =
                                EmailAuthProvider.getCredential(user?.email!!, new_password)
                            user.reauthenticate(credential).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    errorNewPassword("Debe introducir una contraseña distinta")
                                } else {
                                    cambiarPassword()
                                }
                            }
                        } else {
                            errorNewPassword("Debe introducir alemnos 6 caracteres")
                        }
                    } else {
                        errorOldPassword("Contraseña incorrecta")
                    }
                }
            } else {
                errorOldPassword("Debe introducir un campo")
            }
        }else{
            Toast.makeText(this, "Error de Red", Toast.LENGTH_SHORT).show()
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

    private fun cambiarPassword() {
        val user = Firebase.auth.currentUser
        val newPass = binding.EditTextnewPassword.text.toString()

        user!!.updatePassword(newPass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Contraseña cambiada", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun errorOldPassword(mensaje:String){
        binding.EditTextOldPassword.error = mensaje
        binding.ImagenVerPassword.isVisible = false
    }

    private fun errorNewPassword(mensaje: String){
        binding.EditTextnewPassword.error = mensaje
        binding.ImagenverNuevaPass.isVisible = false
    }

    private fun cambiarEstadoImageButton(imagen: ImageView,texto : String?){
        imagen.isVisible = !(texto == null || texto == "")
    }

}