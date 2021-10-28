package garcia.yeray.ucollect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import garcia.yeray.ucollect.databinding.FragmentSignupBinding

class Signup : Fragment() {
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflamos el binding
        binding = FragmentSignupBinding.inflate(layoutInflater)

        //Configuramos los Eventos de los elementos
        binding.TextViewSignUpLogear.setOnClickListener{moverFragment()}

        //Retornamos la vista
        return binding.root
    }

    private fun moverFragment() {
        //cambia de fragment a login
    }
}