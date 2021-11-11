package garcia.yeray.ucollect

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import garcia.yeray.ucollect.databinding.ActivityEditProfileBinding
import garcia.yeray.ucollect.databinding.FragmentPerfilBinding
import java.lang.Exception
import java.lang.StringBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var auth : FirebaseAuth
private val db = FirebaseFirestore.getInstance()

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
    ): View? {
        auth = Firebase.auth
        binding = FragmentPerfilBinding.inflate(layoutInflater)
        binding.ImageViewAdd.setOnClickListener {
            Toast.makeText(activity, "toca imagen", Toast.LENGTH_SHORT).show() }
        binding.buttonEditarPerfil.setOnClickListener { startActivity(Intent(activity,EditProfile::class.java)) }
        binding.ImageViewLogOut.setOnClickListener { logOut() }
        val builder = StringBuilder()
        builder.append(UserData.nombre)
        builder.append(" ")
        builder.append(UserData.apellidos)
        binding.textViewNombre.text = builder.toString()


        // Inflate the layout for this fragment
        return binding.root
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
            startActivity(Intent(activity,MainActivity::class.java))
    }
}