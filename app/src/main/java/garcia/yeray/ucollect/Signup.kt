package garcia.yeray.ucollect
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import garcia.yeray.ucollect.databinding.FragmentPerfilBinding
import garcia.yeray.ucollect.databinding.FragmentSignupBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Signup : Fragment() {
    private lateinit var bindingProfile : FragmentPerfilBinding
    private val storage = Firebase.storage
    private val db = FirebaseFirestore.getInstance()
    private var isPasswordVisible : Boolean = false
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth : FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflamos el binding
        bindingProfile = FragmentPerfilBinding.inflate(layoutInflater)
        binding = FragmentSignupBinding.inflate(layoutInflater)

        //Configuramos los Eventos de los elementos
        binding.TextViewSignUpLogear.setOnClickListener{moverFragment()}
        binding.imageButtonOjoSignUp.isVisible = false
        binding.EditTextSignupPassword.doAfterTextChanged { cambiarEstadoImageButton(binding.EditTextSignupPassword.text.toString()) }
        binding.imageButtonOjoSignUp.setOnClickListener{mostrarPass()}
        binding.buttonSignupRegistro.setOnClickListener {checkValues(binding.editTextSignUpNombre.text.toString(),binding.editTextTextSignUpApellidos.text.toString(),binding.editTextEmailSignup.text.toString(),binding.EditTextSignupPassword.text.toString())}
        auth = Firebase.auth
        //Retornamos la vista
        return binding.root
    }

    private fun checkValues(nombre:String,apellidos:String,email: String,pass: String) {
        if (nombre.isEmpty() || nombre.isBlank()) {
            binding.editTextSignUpNombre.error = "Debe introducir un campo"
        } else if (!checkValidName(nombre)) {
            binding.editTextSignUpNombre.error = "El nombre solo puede contener letras"
        } else if (apellidos.isEmpty() || apellidos.isBlank()) {
            binding.editTextTextSignUpApellidos.error = "Debe introducir un campo"
        } else if (!checkValidApe(apellidos)) {
            binding.editTextTextSignUpApellidos.error = "Apellido no valido"
        } else if (email.isEmpty()) {
            binding.editTextEmailSignup.error = "Debe introducir un campo"
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.editTextEmailSignup.error = "Correo no válido"
        }else {
            auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    if (task.result?.signInMethods?.size != 0) {
                        binding.editTextEmailSignup.error = "Ese correo ya está en uso"
                    } else {
                        if (pass.isEmpty() || pass.isBlank() || pass.length < 6) {
                            binding.EditTextSignupPassword.error =
                                "La contraseña debe tener 6 caracteres"
                            binding.imageButtonOjoSignUp.isVisible = false
                        } else {
                            registrar(email, pass, nombre, apellidos)
                        }
                    }
                }else{
                    showAlert()
                }
            }
        }
    }

    private fun moverFragment() {
        //binding.editTextEmailSignup.setText("")
        //binding.EditTextSignupPassword.setText("")
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

    private fun registrar(email: String, pass  :String,nombre:String,apellido:String){
        if(email.isNotEmpty() && pass.isNotEmpty()) {
            activity?.let {
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(it) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            //Guardamos datos de Usuario
                                UserData.saveUserData(nombre,apellido,"https://firebasestorage.googleapis.com/v0/b/ucollect-20c15.appspot.com/o/images%2Fmodel.png?alt=media&token=0b9fd83a-4bf6-4a89-8fd5-118da75398db")
                                Collections.asignarObjetosLista()
                                startActivity(Intent(activity,Principal::class.java))
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                          showAlert()
                        }
                    }
            }
        }
    }

    private fun showAlert() {
        val view = View.inflate(activity,R.layout.error_registro_layout,null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.btnerrorRegistro).setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun checkValidName(value:String) : Boolean{
        val len = value.length
        for (i in 0 until len) {
            if (!value[i].isLetter()) {
                return false
            }
        }
        return true
    }

    private fun checkValidApe(value: String) : Boolean {
        val len = value.length
        for (i in 0 until len) {
            if (!value[i].isLetter() && value[i].toString() != " ") {
                return false
            }
        }
        return true
    }

}