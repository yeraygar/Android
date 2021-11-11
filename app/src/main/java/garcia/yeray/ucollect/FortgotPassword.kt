package garcia.yeray.ucollect

import android.content.Intent
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
        binding.imageViewAtras.setOnClickListener{this.startActivity(Intent(this,MainActivity::class.java))}
        binding.buttonEnviarForgot.setOnClickListener { enviarEmail(binding.editTextEmailForgot.text.toString()) }
        auth = Firebase.auth
        setContentView(binding.root)
    }

    private fun enviarEmail(email :String){
        if(email.isNotEmpty()) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener{
                task -> if(task.isSuccessful) {
                Toast.makeText(this, "Revise su Email para cambiar la contraseña", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}