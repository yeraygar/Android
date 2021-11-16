package garcia.yeray.ucollect

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import garcia.yeray.ucollect.databinding.ActivityEditProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import kotlin.Exception


class EditProfile : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityEditProfileBinding
    private  val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ intent ->
            if(intent.data?.extras?.get("data") != null) {
                binding.profileImage.setImageBitmap(intent.data?.extras?.get("data") as Bitmap)
                val bitmap = intent.data?.extras?.get("data") as Bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                UserData.data = data
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        binding.imageViewBack.setOnClickListener { preparaIntent() }
        binding.imageViewDone.setOnClickListener { checkValues() }
        binding.btnChangePassword.setOnClickListener { startActivity(Intent(this,Changepassword::class.java)) }
        binding.editTextNombreEditarPerfil.setText(UserData.nombre)
        binding.editTextApellidosEditarPerfil.setText(UserData.apellidos)
        binding.editTextEmailEditarPerfil.setText(UserData.email)
        binding.btnChangeEmail.setOnClickListener { startActivity(Intent(this,ChangeEmail::class.java))}
        binding.editTextEmailEditarPerfil.isEnabled = false
        binding.profileImage.setOnClickListener{
            abrirCamara()
        }
        Glide.with(this).load(UserData.urlImg).into(binding.profileImage)
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
        //val email = binding.editTextEmailEditarPerfil
        if(nombre.text.toString().isEmpty() || nombre.text.toString().isBlank()) {
            nombre.error = "Debe introducir un nombre"
        }else if (!checkValidName(nombre.text.toString())) {
            nombre.error = "Introduzca un nombre válido"
        }else if (apellidos.text.toString().isEmpty() || apellidos.text.toString().isBlank()){
            apellidos.error = "Debes introducir los apellidos"
        }else if (!checkValidApe(apellidos.text.toString())) {
            apellidos.error = "Introduzca unos apellidos válidos"
        }else {
            actualizarPerfil(nombre.text.toString(),apellidos.text.toString())
        }
    }
    
    private fun actualizarPerfil(nombre : String, apellidos : String) {
        if (UserData.data != null) {
            UserData.uploadImage(UserData.data!!)
        }

        if (nombre != UserData.nombre) {
            UserData.changeName(nombre)
        }

        if (apellidos != UserData.apellidos) {
            UserData.changeLastName(apellidos)
        }

        Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
    }

    private fun abrirCamara() {
        getResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    private fun checkValidName(value:String) : Boolean{
        val len = value.length
        for (i in 0 until len) {
            if (!value[i].isLetter()) {
                return false
            }
        }
        return true
    }

    private fun checkValidApe(value: String) : Boolean {
        val len = value.length
        for (i in 0 until len) {
            if (!value[i].isLetter() && value[i].toString() != " ") {
                return false
            }
        }
        return true
    }
}