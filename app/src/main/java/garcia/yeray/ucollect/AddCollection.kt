package garcia.yeray.ucollect

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import garcia.yeray.ucollect.databinding.ActivityAddCollectionBinding


class AddCollection : AppCompatActivity() {
    private val elementosInter = arrayOf("Aceptar intercambio","Denegar intercambio")
    private var elementosTipo = arrayOf("Música","Películas","Videojuegos","Juegos de mesa","Cartas","Cromos","Vehículos","Otros")
    private lateinit var binding: ActivityAddCollectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCollectionBinding.inflate(layoutInflater)
        val adapter = ArrayAdapter(this,R.layout.spinner_item_intercambio,elementosInter)
        val adapterTipo = ArrayAdapter(this,R.layout.spinner_item_intercambio,elementosTipo)
        binding.spinnerTipo.adapter = adapterTipo
        binding.spinnerIntercambio.adapter = adapter
        binding.ImageViewObject.setOnClickListener { abrirGaleria() }
        binding.imageViewBack.setOnClickListener { onBackPressed() }
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
            val progresDialog = ProgressDialog(this)
            progresDialog.setMessage("Subiendo Objeto...")
            progresDialog.setCancelable(false)
            progresDialog.show()
            val nombre = binding.editTextTextPersonName.text.toString()
            val tipo = binding.spinnerTipo.selectedItem.toString()
            val precio = binding.editTextNumberDecimal.text.toString()
            val intercambio = binding.spinnerIntercambio.selectedItem.toString()
            val db = FirebaseFirestore.getInstance()
            val coleccion = db.collection("collection").document()
            coleccion.set(
                hashMapOf(
                    "urlImagen" to "urlImagen",
                    "nombreObjeto" to nombre,
                    "tipoObjeto" to tipo,
                    "precioObjeto" to precio,
                    "intercambioObjeto" to intercambio,
                    "userEmail" to UserData.email
                )
            ).addOnFailureListener{
                Toast.makeText(this, "Fallo al guardar el objeto", Toast.LENGTH_SHORT).show()
                if (progresDialog.isShowing) progresDialog.dismiss()
            }.addOnSuccessListener {
                val referenceToImage = Firebase.storage.reference.child("CollectionImages/"+coleccion.id)
                val addImage = referenceToImage.putFile(UserCollections.ImageDataFromCollection!!)
                addImage.addOnFailureListener{
                    Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
                    if (progresDialog.isShowing) progresDialog.dismiss()
                }.addOnSuccessListener {
                    referenceToImage.downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val url = task.result.toString()
                            coleccion.update("urlImagen",url).addOnFailureListener{
                                Toast.makeText(this, "Fallo al actualizar url Imagen", Toast.LENGTH_SHORT).show()
                                if (progresDialog.isShowing) progresDialog.dismiss()
                            }.addOnSuccessListener {
                                UserCollections.objetos.clear()
                                val bd = Firebase.firestore
                                bd.collection("collection").whereEqualTo("userEmail", UserData.email!!).get().addOnFailureListener{
                                    Toast.makeText(this, "Fallo al obtener los objetos BD", Toast.LENGTH_SHORT).show()
                                    if (progresDialog.isShowing) progresDialog.dismiss()
                                }.addOnSuccessListener { task ->
                                    if (task != null) {
                                        val documents = task.documents
                                        for (document in documents) {
                                            bd.collection("collection")
                                                .document(document.id).get().addOnFailureListener {
                                                    Toast.makeText(this, "Fallo al obtener un Objeto BD", Toast.LENGTH_SHORT).show()
                                                    if (progresDialog.isShowing) progresDialog.dismiss()
                                                }.addOnSuccessListener {
                                                    val urlImg = it.get("urlImagen")
                                                    val nombre = it.get("nombreObjeto")
                                                    val tipo = it.get("tipoObjeto")
                                                    val precio = it.get("precioObjeto")
                                                    val intercambio = it.get("intercambioObjeto")
                                                    val userEmail = it.get("userEmail")
                                                    val objeto = Objeto(urlImg.toString(),nombre.toString(),tipo.toString(),precio.toString(),intercambio.toString(),document.id,userEmail.toString())
                                                    UserCollections.objetos.add(objeto)
                                                    onBackPressed()
                                                }
                                        }
                                        Toast.makeText(this, "El Objeto ha sido guardado en la base de datos", Toast.LENGTH_SHORT).show()
                                        if (progresDialog.isShowing) progresDialog.dismiss()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else {
            Toast.makeText(this, "Hay campos sin completar", Toast.LENGTH_SHORT).show()
        }

    }
}