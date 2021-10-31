package garcia.yeray.ucollect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import garcia.yeray.ucollect.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val adapter by lazy { ViewPagerAdapterLogin(this) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.pager.setPageTransformer(TransformadorVista())
        binding.pager.adapter = adapter
    }
}