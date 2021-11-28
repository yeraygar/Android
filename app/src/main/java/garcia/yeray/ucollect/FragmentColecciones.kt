package garcia.yeray.ucollect

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Value
import garcia.yeray.ucollect.databinding.FragmentColeccionesBinding
import org.w3c.dom.Document
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentColecciones.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentColecciones : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
   private lateinit var recyclerview : RecyclerView
   private lateinit var coleccionesArray : ArrayList<Objeto>
   private lateinit var myAdapter : ObjetoAdapterCollections
   private lateinit var db : FirebaseFirestore
    private lateinit var binding : FragmentColeccionesBinding

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
        // Inflate the layout for this fragment
        binding = FragmentColeccionesBinding.inflate(layoutInflater)
        recyclerview = binding.recyclerViewColecciones
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.setHasFixedSize(true)
        coleccionesArray = arrayListOf()
        myAdapter = ObjetoAdapterCollections(coleccionesArray)
        recyclerview.adapter = myAdapter
        EventChangeListener()
        return binding.root
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("collection").whereNotEqualTo("userEmail", UserData.email!!).addSnapshotListener(
            object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firebase Error",error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val urlImg = dc.document.get("urlImagen")
                            val nombre = dc.document.get("nombreObjeto")
                            val tipo = dc.document.get("tipoObjeto")
                            val precio = dc.document.get("precioObjeto")
                            val intercambio = dc.document.get("intercambioObjeto")
                            val userEmail = dc.document.get("userEmail")
                            coleccionesArray.add(Objeto(urlImg.toString(),nombre.toString(),tipo.toString(),precio.toString(),intercambio.toString(),dc.document.id,userEmail.toString()))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            }
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentColecciones.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentColecciones().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}