package garcia.yeray.ucollect

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import garcia.yeray.ucollect.databinding.ActivityEditProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream


class EditProfile : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityEditProfileBinding
    private var data : ByteArray? = null
    private var bitmap : Bitmap? = null
    private  val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ intent ->
            if(intent.data?.extras?.get("data") != null) {
                binding.profileImage.setImageBitmap(intent.data?.extras?.get("data") as Bitmap)
                bitmap = intent.data?.extras?.get("data") as Bitmap
                //UserData.bitmapImg = bitmap
                val baos = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                data = baos.toByteArray()
                //UserData.data = data
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        binding.imageViewBack.setOnClickListener { returnIntent()}
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

    override fun onResume() {
        super.onResume()
        binding.editTextNombreEditarPerfil.setText(UserData.nombre)
        binding.editTextApellidosEditarPerfil.setText(UserData.apellidos)
        binding.editTextEmailEditarPerfil.setText(UserData.user!!.email)
    }

    private fun returnIntent() {
        val intent = Intent()
        intent.putExtra("bitmap",UserData.dataFragmentPerfil)
        setResult(RESULT_OK,intent)
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
        if (data != null) {
            UserData.bitmapImg = bitmap
            UserData.data = data
            UserData.uploadImage(UserData.data!!)
            UserData.dataFragmentPerfil = UserData.data

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