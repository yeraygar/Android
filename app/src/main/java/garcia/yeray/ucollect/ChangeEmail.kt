package garcia.yeray.ucollect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.auth.User
import garcia.yeray.ucollect.databinding.ActivityChangeEmailBinding
import java.lang.Exception

class ChangeEmail : AppCompatActivity() {
    private lateinit var binding : ActivityChangeEmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeEmailBinding.inflate(layoutInflater)
        binding.ImagenVerPasswordVerificarCuenta.isVisible = false
        binding.imageViewBack.setOnClickListener { onBackPressed()}
        binding.ImagenVerPasswordVerificarCuenta.setOnClickListener { changeVisibilityMode(binding.ImagenVerPasswordVerificarCuenta,binding.EditTextPasswordVerificarCuenta)}
        binding.EditTextPasswordVerificarCuenta.doAfterTextChanged { cambiarEstadoImageButton(binding.ImagenVerPasswordVerificarCuenta,binding.EditTextPasswordVerificarCuenta.text.toString())}
        binding.btnAceptar.setOnClickListener { nextView(binding.EditTextPasswordVerificarCuenta) }
        setContentView(binding.root)
    }

    private fun nextView(password:EditText){
        if(password.text.toString().isEmpty() || password.text.isBlank()) {
            password.error = "Debe introducir la contraseña"
        }else {
                var credential = EmailAuthProvider.getCredential(UserData.email!!, password.text.toString())
                UserData.user?.reauthenticate(credential)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        UserData.userPassword = password.text.toString()
                        startActivity(Intent(this,ChangeEmailVerified::class.java))
                        finish()
                    }else{
                        binding.EditTextPasswordVerificarCuenta.error = "Contraseña incorrecta"
                        binding.ImagenVerPasswordVerificarCuenta.isVisible = false
                    }
                }

        }
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

    private fun cambiarEstadoImageButton(imagen: ImageView,texto : String?){
        imagen.isVisible = !(texto == null || texto == "")
    }
}