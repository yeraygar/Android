package garcia.yeray.ucollect

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import garcia.yeray.ucollect.databinding.ActivityAddCollectionBinding
import java.io.ByteArrayOutputStream

class AddCollection : AppCompatActivity() {
    val elementosInter = arrayOf("Aceptar intercambio","Denegar intercambio")
    var elementosTipo = arrayOf("Música","Películas","Videojuegos","Juegos de mesa","Cartas","Cromos","Vehículos","Otros")
    private lateinit var binding: ActivityAddCollectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCollectionBinding.inflate(layoutInflater)
        val adapter = ArrayAdapter(this,R.layout.spinner_item_intercambio,elementosInter)
        val adapterTipo = ArrayAdapter(this,R.layout.spinner_item_intercambio,elementosTipo)
        binding.spinnerTipo.adapter = adapterTipo
        binding.spinnerIntercambio.adapter = adapter
        binding.ImageViewObject.setOnClickListener { abrirGaleria() }
        binding.imageViewBack.setOnClickListener { preparaIntent() }
        binding.btnAceptar.setOnClickListener { realizarCambios() }
        setContentView(binding.root)
    }

    private fun abrirGaleria() {
       val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            binding.ImageViewObject.setImageURI(data)
            UserCollections.ImageDataFromCollection = data
        }

    }

    private fun realizarCambios(){
        if(UserCollections.ImageDataFromCollection != null && binding.editTextTextPersonName.text.toString().isNotEmpty() && binding.editTextTextPersonName.text.toString().isNotBlank() && binding.editTextNumberDecimal.text.toString().isNotEmpty() && binding.editTextNumberDecimal.text.toString().isNotBlank()) {
            val nombre = binding.editTextTextPersonName.text.toString()
            val tipo = binding.spinnerTipo.selectedItem.toString()
            val precio = binding.editTextNumberDecimal.text.toString().toDouble()
            val intercambio = binding.spinnerIntercambio.selectedItem.toString() == "Aceptar intercambio"
            UserCollections.uploadToFirebase(UserCollections.ImageDataFromCollection!!,nombre,tipo,precio,intercambio)
        }else {
            Toast.makeText(this, "Hay campos sin completar", Toast.LENGTH_SHORT).show()
            //showAlert()
        }

    }

    private fun showAlert() {
        //
    }

    private fun preparaIntent(){
        val intent = Intent(this,Principal::class.java)
        val bundle = Bundle()
        bundle.putString("tipo","perfil")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}