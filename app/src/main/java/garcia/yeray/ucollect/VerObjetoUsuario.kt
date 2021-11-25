package garcia.yeray.ucollect

import android.app.Activity
import android.app.ProgressDialog
import android.content.AsyncQueryHandler
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import garcia.yeray.ucollect.databinding.ActivityVerObjetoUsuarioBinding
import kotlinx.coroutines.delay
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class VerObjetoUsuario : AppCompatActivity() {
    private val elementosInter = arrayOf("Aceptar intercambio","Denegar intercambio")
    private var elementosTipo = arrayOf("Música","Películas","Videojuegos","Juegos de mesa","Cartas","Cromos","Vehículos","Otros")
    private var imagen : Uri? = null
    private lateinit var binding : ActivityVerObjetoUsuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerObjetoUsuarioBinding.inflate(layoutInflater)
        binding.editTextTextPersonName.setText(intent.getStringExtra("nombre"))
        val adapter = ArrayAdapter(this,R.layout.spinner_item_intercambio,elementosTipo)
        val tipoAdapter = ArrayAdapter(this,R.layout.spinner_item_intercambio,elementosInter)
        binding.spinnerTipo.adapter = adapter
        binding.spinnerTipo.setSelection(adapter.getPosition(intent.getStringExtra("tipo")))
        binding.editTextNumberDecimal.setText(intent.getStringExtra("precio"))
        binding.spinnerIntercambio.adapter = tipoAdapter
        binding.spinnerIntercambio.setSelection(tipoAdapter.getPosition(intent.getStringExtra("intercambio")))
        loadImage()
        binding.imageViewBack.setOnClickListener { onBackPressed() }
        binding.objectImage.setOnClickListener { abrirGaleria() }
        binding.btnGuardar.setOnClickListener { actualizarObjeto() }
        binding.imageViewDelete.setOnClickListener { eliminarObjeto() }
        Log.d("objeto abrir","${intent.getStringExtra("nombre")} / ${intent.getStringExtra("id")}")
        setContentView(binding.root)
    }

    private fun loadImage() {
        Glide.with(this).load(intent.getStringExtra("urlImg")).into(binding.objectImage)
    }

    private fun actualizarObjeto() {
        if (binding.editTextTextPersonName.text.isNotEmpty() && binding.editTextTextPersonName.text.isNotBlank() && binding.editTextNumberDecimal.text.isNotEmpty() && binding.editTextNumberDecimal.text.isNotBlank()){
            val progresDialog = ProgressDialog(this)
            progresDialog.setMessage("Actualizando...")
            progresDialog.setCancelable(false)
            progresDialog.show()

            val nombre = binding.editTextTextPersonName.text.toString()
            val tipo = binding.spinnerTipo.selectedItem.toString()
            val precio = binding.editTextNumberDecimal.text.toString()
            val intercambio = binding.spinnerIntercambio.selectedItem.toString()
            val db = FirebaseFirestore.getInstance()
            val coleccion = db.collection("collection").document(intent.getStringExtra("id")!!)

            if(imagen != null) {
                val imgReference = Firebase.storage.reference.child("CollectionImages/" + intent.getStringExtra("id"))
                val uploadTask = imgReference.putFile(imagen!!)
                uploadTask.addOnFailureListener {
                    Toast.makeText(this, "Fallo al subir la imagen", Toast.LENGTH_SHORT).show()
                    if (progresDialog.isShowing) progresDialog.dismiss()
                }.addOnSuccessListener {
                    imgReference.downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                          val newUrl = task.result.toString()
                            coleccion.update(
                                "urlImagen", newUrl,
                            "nombreObjeto",nombre,
                                "tipoObjeto",tipo,
                            "preciObjeto", precio,
                            "intercambioObjeto",intercambio).addOnFailureListener {
                                Toast.makeText(this, "Fallo al Actualizar la imagen en la coleccion", Toast.LENGTH_SHORT).show()
                                if (progresDialog.isShowing) progresDialog.dismiss()
                            }.addOnSuccessListener {
                                UserCollections.asignarObjetosLista()
                                Toast.makeText(this, "Los datos han sido actualizados", Toast.LENGTH_SHORT).show()
                                if (progresDialog.isShowing) progresDialog.dismiss()
                            }
                        }
                    }
                }
            }else{
                coleccion.update("nombreObjeto",nombre,
                "tipoObjeto",tipo,
                "precioObjeto",precio,
                "intercambioObjeto",intercambio).addOnFailureListener{
                    Toast.makeText(this, "Fallo al actualizar los datos", Toast.LENGTH_SHORT).show()
                    if (progresDialog.isShowing) progresDialog.dismiss()
                }.addOnSuccessListener {
                    UserCollections.asignarObjetosLista()
                    if (progresDialog.isShowing) progresDialog.dismiss()
                    Toast.makeText(this, "Los datos han sido actualizados", Toast.LENGTH_SHORT).show()
                }
            }

        }else{
            Toast.makeText(this, "Hay campos sin completar", Toast.LENGTH_SHORT).show()
        }

    }

    private val startForActivityChangeImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            binding.objectImage.setImageURI(data)
            //UserCollections.ImageDataFromCollection = data
            imagen = data
        }

    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityChangeImage.launch(intent)
    }

    private fun eliminarObjeto() {
        val progresDialog = ProgressDialog(this)
        progresDialog.setMessage("Eliminando...")
        progresDialog.setCancelable(false)
        progresDialog.show()
        //eliminamos la imagen
        val reference = Firebase.storage.reference.child("CollectionImages/"+intent.getStringExtra("id"))
        reference.delete().addOnSuccessListener {
            //eliminamos la coleccion
            val bd = FirebaseFirestore.getInstance()
            bd.collection("collection").document(intent.getStringExtra("id")!!).delete().addOnSuccessListener {
                //Limpiamos y asignamos los objetos a la lista
                val index = intent.getIntExtra("indexUserCollection",0)
                UserCollections.objetos.removeAt(index)
                }.addOnCompleteListener {
                    progresDialog.dismiss()
                    onBackPressed()
                }.addOnFailureListener {
                Toast.makeText(this, "Fallo al eliminar el objeto", Toast.LENGTH_SHORT).show()
                progresDialog.dismiss()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al borrar el objeto", Toast.LENGTH_SHORT).show()
            progresDialog.dismiss()
        }
    }
}