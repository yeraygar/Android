package garcia.yeray.ucollect

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import garcia.yeray.ucollect.databinding.ActivityEditProfileBinding
import android.R.attr.data



class EditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private  val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ intent ->
          binding.profileImage.setImageBitmap(intent.data?.extras?.get("data") as Bitmap)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        binding.imageViewBack.setOnClickListener { preparaIntent() }
        binding.imageViewDone.setOnClickListener { checkValues() }
        binding.btnChangePassword.setOnClickListener { startActivity(Intent(this,Changepassword::class.java)) }
        binding.editTextNombreEditarPerfil.setText(UserData.nombre)
        binding.editTextApellidosEditarPerfil.setText(UserData.apellidos)
        binding.editTextEmailEditarPerfil.setText(UserData.email)
        binding.profileImage.setOnClickListener{
            abrirCamara()
        }
        setContentView(binding.root)
    }

    private fun preparaIntent(){
        val intent = Intent(this,Principal::class.java)
        val bundle = Bundle()
        bundle.putString("tipo","perfil")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
    
    private fun checkValues() {
        val nombre = binding.editTextNombreEditarPerfil
        val apellidos = binding.editTextApellidosEditarPerfil
        val email = binding.editTextEmailEditarPerfil
        if(nombre.text.toString().isEmpty()) {
            nombre.error = "Debe introducir un nombre"
        }
        if(apellidos.text.toString().isEmpty()){
            apellidos.error = "Debes introducir los apellidos"
        }
        if(email.text.toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Email no válido"
        }
        if(nombre.text.toString().isNotEmpty() && apellidos.text.toString().isNotEmpty() && email.text.toString().isNotEmpty()) {
            actualizarPerfil(nombre.text.toString(),apellidos.text.toString(),email.text.toString())
        }
    }
    
    private fun actualizarPerfil(nombre : String, apellidos : String, email : String) {
        Toast.makeText(this, "Realiza el cambio", Toast.LENGTH_SHORT).show()
    }

    private fun abrirCamara() {
        getResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

}