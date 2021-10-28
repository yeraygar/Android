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
import garcia.yeray.ucollect.databinding.FragmentLoginBinding

class Login : Fragment() {
    private lateinit var bindin : FragmentLoginBinding
    private var isPasswordVisible : Boolean = false
    //private val adapter by lazy { ViewPagerAdapterLogin(this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflamos el binding al crear el fragmento
        bindin = FragmentLoginBinding.inflate(layoutInflater)

        //Configuramos los setOnclickListeners
        bindin.buttonLoginAcceder.setOnClickListener {acceder() }
        bindin.imageButtonOjoLogin.isVisible = false
        bindin.EditTextLoginPassword.doAfterTextChanged {cambiarEstadoImageButton(bindin.EditTextLoginPassword.text.toString())}
        bindin.imageButtonOjoLogin.setOnClickListener{mostrarPass()}
        bindin.TextViewLoginRegistrar.setOnClickListener{moverFragment()}


        //retornamos la vista (root)
        return bindin.root

    }

    private fun acceder(){
        //haz login
    }

    private fun mostrarPass(){
       if(!isPasswordVisible) {
           isPasswordVisible = true
           bindin.EditTextLoginPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
           bindin.imageButtonOjoLogin.setImageResource(R.drawable.ic_baseline_visibility_off_24)
       }else{
           isPasswordVisible = false
           bindin.EditTextLoginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
           bindin.imageButtonOjoLogin.setImageResource(R.drawable.ic_baseline_visibility_24)
       }
    }

    private fun moverFragment(){

    }

    private fun cambiarEstadoImageButton(texto : String?){
        bindin.imageButtonOjoLogin.isVisible = !(texto == null || texto == "")
    }

}