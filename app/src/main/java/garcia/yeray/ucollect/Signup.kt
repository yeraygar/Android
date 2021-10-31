package garcia.yeray.ucollect

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.viewpager2.widget.ViewPager2
import garcia.yeray.ucollect.databinding.FragmentSignupBinding

class Signup : Fragment() {
    private var isPasswordVisible : Boolean = false
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflamos el binding
        binding = FragmentSignupBinding.inflate(layoutInflater)

        //Configuramos los Eventos de los elementos
        binding.TextViewSignUpLogear.setOnClickListener{moverFragment()}
        binding.imageButtonOjoSignUp.isVisible = false
        binding.EditTextSignupPassword.doAfterTextChanged { cambiarEstadoImageButton(binding.EditTextSignupPassword.text.toString()) }
        binding.imageButtonOjoSignUp.setOnClickListener{mostrarPass()}
        binding.buttonSignupRegistro.setOnClickListener { registrar() }

        //Retornamos la vista
        return binding.root
    }

    private fun moverFragment() {
        binding.editTextEmailSignup.setText("")
        binding.EditTextSignupPassword.setText("")
        requireActivity().findViewById<ViewPager2>(R.id.pager).currentItem = 0
    }

    private fun mostrarPass(){
        if(!isPasswordVisible) {
            isPasswordVisible = true
            binding.EditTextSignupPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.imageButtonOjoSignUp.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }else{
            isPasswordVisible = false
            binding.EditTextSignupPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.imageButtonOjoSignUp.setImageResource(R.drawable.ic_baseline_visibility_24)
        }
    }

    private fun cambiarEstadoImageButton(texto : String?){
        //Hace visible el elemento si el texto no es null o es igual a ""
        binding.imageButtonOjoSignUp.isVisible = !(texto == null || texto == "")
    }

    private fun registrar(){

    }

}