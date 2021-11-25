package garcia.yeray.ucollect

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import garcia.yeray.ucollect.databinding.ActivityVerObjetoUsuarioBinding
import garcia.yeray.ucollect.databinding.ActivityVerObjetosBinding

class verObjetos : AppCompatActivity() {
    private lateinit var binding: ActivityVerObjetosBinding
    private val elementosInter = arrayOf("Aceptar intercambio","Denegar intercambio")
    private var elementosTipo = arrayOf("Música","Películas","Videojuegos","Juegos de mesa","Cartas","Cromos","Vehículos","Otros")
    private var imagen : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerObjetosBinding.inflate(layoutInflater)
        binding.editTextTextPersonName.setText(intent.getStringExtra("nombreObjeto"))
        val adapter = ArrayAdapter(this,R.layout.spinner_item_intercambio,elementosTipo)
        val tipoAdapter = ArrayAdapter(this,R.layout.spinner_item_intercambio,elementosInter)
        binding.spinnerTipo.adapter = adapter
        binding.spinnerTipo.setSelection(adapter.getPosition(intent.getStringExtra("tipo")))
        binding.editTextNumberDecimal.setText(intent.getStringExtra("precio"))
        binding.spinnerIntercambio.adapter = tipoAdapter
        binding.spinnerIntercambio.setSelection(tipoAdapter.getPosition(intent.getStringExtra("intercambio")))
        binding.editTextTextUserEmail.setText(UserData.email)
        loadImage()
        binding.imageViewBack.setOnClickListener { onBackPressed() }
        setContentView(binding.root)
    }

    private fun loadImage() {
        Glide.with(this).load(intent.getStringExtra("urlImg")).into(binding.objectImage2)
    }
}