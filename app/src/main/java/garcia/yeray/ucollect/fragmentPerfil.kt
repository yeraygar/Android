package garcia.yeray.ucollect

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import garcia.yeray.ucollect.databinding.FragmentPerfilBinding
import java.lang.StringBuilder
import android.net.ConnectivityManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.ByteArrayOutputStream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var auth : FirebaseAuth
private val storage = Firebase.storage

/**
 * A simple [Fragment] subclass.
 * Use the [fragmentPerfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragmentPerfil : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        binding = FragmentPerfilBinding.inflate(layoutInflater)
        //initRecycler()
        binding.ImageViewAdd.setOnClickListener { startActivity(Intent(activity,AddCollection::class.java))}
        binding.buttonEditarPerfil.setOnClickListener { checkAndStartActivity() }
        binding.ImageViewLogOut.setOnClickListener { logOut() }
        val builder = StringBuilder()
        builder.append(UserData.nombre)
        builder.append(" ")
        builder.append(UserData.apellidos)
        if(UserData.bitmapImg == null) {
            Glide.with(this).load(UserData.urlImg).into(binding.profileImage)
        }else{
            binding.profileImage.setImageBitmap(UserData.bitmapImg)
        }


        binding.textViewNombre.text = builder.toString()

        initRecycler()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecycler()
    }

    override fun onResume() {
        super.onResume()
        Log.d("objetos_perfil",UserCollections.objetos.size.toString())
        initRecycler()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragmentPerfil.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragmentPerfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun logOut(){
            auth.signOut()
            UserData.resetData()
            UserCollections.objetos.clear()
            Collections.objetos.clear()
            startActivity(Intent(activity,MainActivity::class.java))
            requireActivity().finish()
    }

    private fun checkAndStartActivity() {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if(networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected) {
            getResultEditText.launch(Intent(activity,EditProfile::class.java))
        }else{
            Toast.makeText(activity, "Error de red", Toast.LENGTH_SHORT).show()
        }
    }


    private fun initRecycler() {
        binding.recyclerViewObjetoUsuario.layoutManager = LinearLayoutManager(activity)
        val adapter = ObjetoAdapter(UserCollections.objetos)
        binding.recyclerViewObjetoUsuario.adapter = adapter
    }

    private val getResultEditText = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
    ){ result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val byteArray = result.data?.getByteArrayExtra("bitmap")
            if (byteArray != null) {
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                UserData.bitmapImg = bitmap
                binding.profileImage.setImageBitmap(bitmap)
            }
        }

    }
}