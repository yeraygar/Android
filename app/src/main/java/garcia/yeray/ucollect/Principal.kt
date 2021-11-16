package garcia.yeray.ucollect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import garcia.yeray.ucollect.databinding.ActivityPrincipalBinding

class Principal() : AppCompatActivity() {
    private val fragmentColecciones = FragmentColecciones()
    private val fragmentPerfil = fragmentPerfil()
    private lateinit var binding : ActivityPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = intent.extras
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        //loadFragment(fragmentColecciones)
        if(extras == null) {
            loadFragment(fragmentColecciones)
            binding.bottomNavigation.selectedItemId = R.id.firstFragment
            binding.bottomNavigation.menu.findItem(R.id.firstFragment).setIcon(R.drawable.galerian)
            binding.bottomNavigation.menu.findItem(R.id.secondFragment).setIcon(R.drawable.usu)
        }else if(extras.getString("tipo").equals("colecciones")) {
            loadFragment(fragmentColecciones)
            binding.bottomNavigation.selectedItemId = R.id.firstFragment
            binding.bottomNavigation.menu.findItem(R.id.firstFragment).setIcon(R.drawable.galerian)
            binding.bottomNavigation.menu.findItem(R.id.secondFragment).setIcon(R.drawable.usu)
        }else if(extras.getString("tipo").equals("perfil")){
            loadFragment(fragmentPerfil)
            binding.bottomNavigation.selectedItemId = R.id.secondFragment
            binding.bottomNavigation.menu.findItem(R.id.firstFragment).setIcon(R.drawable.galeria)
            binding.bottomNavigation.menu.findItem(R.id.secondFragment).setIcon(R.drawable.blackusu)
        }else {
            loadFragment(fragmentColecciones)
            binding.bottomNavigation.selectedItemId = R.id.firstFragment
            binding.bottomNavigation.menu.findItem(R.id.firstFragment).setIcon(R.drawable.galerian)
            binding.bottomNavigation.menu.findItem(R.id.secondFragment).setIcon(R.drawable.usu)
        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.firstFragment -> {
                    loadFragment(fragmentColecciones)
                    item.setIcon(R.drawable.galerian)
                    binding.bottomNavigation.menu.findItem(R.id.secondFragment).setIcon(R.drawable.usu)
                }
                R.id.secondFragment -> {
                    loadFragment(fragmentPerfil)
                    item.setIcon(R.drawable.blackusu)
                    binding.bottomNavigation.menu.findItem(R.id.firstFragment).setIcon(R.drawable.galeria)
                }else -> {
                    loadFragment(fragmentColecciones)
                }
            }
            true
        }
        setContentView(binding.root)
    }

    private fun loadFragment(fragment : Fragment) {
      val transaction =  supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container,fragment)
        transaction.commit()
    }

}