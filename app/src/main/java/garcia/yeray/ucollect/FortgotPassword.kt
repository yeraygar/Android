package garcia.yeray.ucollect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import garcia.yeray.ucollect.databinding.ActivityFortgotPasswordBinding
import garcia.yeray.ucollect.databinding.FragmentSignupBinding

class FortgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityFortgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFortgotPasswordBinding.inflate(layoutInflater)
        binding.imageViewAtras.setOnClickListener{this.startActivity(Intent(this,MainActivity::class.java))}
        setContentView(binding.root)
    }

    private fun enviarEmail(){

    }
}