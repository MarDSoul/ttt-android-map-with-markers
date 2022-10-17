package app.mardsoul.mapmarkers.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.mardsoul.mapmarkers.R
import app.mardsoul.mapmarkers.databinding.ActivityMainBinding
import app.mardsoul.mapmarkers.ui.map.MapsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, MapsFragment.newInstance())
                .commit()
        }
    }
}