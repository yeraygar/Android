package garcia.yeray.ucollect

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import garcia.yeray.ucollect.databinding.ActivityVerObjetoUsuarioBinding
import garcia.yeray.ucollect.databinding.ActivityVerObjetosBinding

class verObjetos : AppCompatActivity() {
    private lateinit var binding: ActivityVerObjetosBinding
    //private val elementosInter = arrayOf("Aceptar intercambio","Denegar intercambio")
    //private var elementosTipo = arrayOf("Música","Películas","Videojuegos","Juegos de mesa","Cartas","Cromos","Vehículos","Otros")
    private var imagen : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerObjetosBinding.inflate(layoutInflater)
        binding.editTextTextPersonName.text = intent.getStringExtra("nombre")
        binding.editTextTextPersonName.movementMethod = ScrollingMovementMethod()
        binding.spinnerTipo.text = intent.getStringExtra("tipo")
        binding.editTextNumberDecimal.text = intent.getStringExtra("precio")
        binding.spinnerIntercambio.text = intent.getStringExtra("intercambio")
        binding.editTextTextUserEmail.text = intent.getStringExtra("userEmail")
        binding.editTextTextUserEmail.isSelected = true
        loadImage()
        binding.imageViewBack.setOnClickListener { onBackPressed() }
        setContentView(binding.root)
    }

    private fun loadImage() {
        Glide.with(this).load(intent.getStringExtra("urlImg")).into(binding.objectImage2)
    }
}