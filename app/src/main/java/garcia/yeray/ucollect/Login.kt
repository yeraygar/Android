package garcia.yeray.ucollect

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import garcia.yeray.ucollect.databinding.FragmentLoginBinding
import java.lang.Exception
import kotlin.concurrent.thread

class Login : Fragment() {
    private lateinit var bindin : FragmentLoginBinding
    private var isPasswordVisible : Boolean = false
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflamos el binding al crear el fragmento
        auth = Firebase.auth
        bindin = FragmentLoginBinding.inflate(layoutInflater)

        //Configuramos los setOnclickListeners
        bindin.buttonLoginAcceder.setOnClickListener {checkValues(bindin.editTextEmailLogin.text.toString(),bindin.EditTextLoginPassword.text.toString())}
        bindin.imageButtonOjoLogin.isVisible = false
        bindin.EditTextLoginPassword.doAfterTextChanged {cambiarEstadoImageButton(bindin.EditTextLoginPassword.text.toString())}
        bindin.imageButtonOjoLogin.setOnClickListener{mostrarPass()}
        bindin.TextViewLoginRegistrar.setOnClickListener{moverFragment()}
        bindin.textViewForgot.setOnClickListener{this.startActivity(Intent(activity,FortgotPassword::class.java))}


        //retornamos la vista (root)
        return bindin.root

    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user != null) {
            reload()
        }
    }

    private fun checkValues(email: String,password: String) {
       if(email.isEmpty()) {
            errorMail("Debe introducir un campo")
       }else {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                errorMail("Debe introducir un correo válido")
            }else{
                auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
                    if(task.result?.signInMethods?.size == 0) {
                        errorMail("No existe ningún usuario con esa cuenta")
                    }else{
                       if(password.isEmpty()) {
                            errorPass(bindin.imageButtonOjoLogin,"Debe introducir la contraseña")
                       }else{
                           acceder(email, password)
                       }
                    }
                }
            }
       }
    }

    private fun reload() {
        //Guardamos los datos de este usuario
        UserData.restoreUserData()
        startActivity(Intent(activity,Principal::class.java))
    }

    private fun acceder(email : String, password: String){
        activity?.let {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(it) { task ->
                    if (!task.isSuccessful) {
                        try {
                            throw task.exception!!
                        }catch (e: FirebaseAuthInvalidCredentialsException) {
                            errorPass(bindin.imageButtonOjoLogin,"Contraseña incorrecta")
                        }catch (e: Exception) {
                            showAlert()
                        }
                    } else {
                        reload()
                    }
                }
        }
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
        bindin.editTextEmailLogin.setText("")
        bindin.EditTextLoginPassword.setText("")
        requireActivity().findViewById<ViewPager2>(R.id.pager).currentItem = 1
    }

    private fun cambiarEstadoImageButton(texto : String?){
        bindin.imageButtonOjoLogin.isVisible = !(texto == null || texto == "")
    }

    private fun showAlert()  {
        val view = View.inflate(activity,R.layout.error_inicio_sesion,null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.errorInicio).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun errorMail(mensaje:String) {
        bindin.editTextEmailLogin.error = mensaje
    }

    private fun errorPass(imageView: ImageView,mensaje: String) {
        imageView.isVisible = false
        bindin.EditTextLoginPassword.error = mensaje
    }
}