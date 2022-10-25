package no.kasperi.matoppskrifter.aktiviteter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.databinding.ActivityMainBinding
import no.kasperi.matoppskrifter.db.OppskriftDB
import no.kasperi.matoppskrifter.viewModel.HjemViewModel
import no.kasperi.matoppskrifter.viewModel.HjemViewModelFactory

class MainActivity : AppCompatActivity() {
    val viewModel: HjemViewModel by lazy {
       val oppskriftDB = OppskriftDB.hentInstance(this)
        val hjemViewModelProviderFactory = HjemViewModelFactory(oppskriftDB)
        ViewModelProvider(this, hjemViewModelProviderFactory)[HjemViewModel::class.java]
    }
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bunn_navigation)
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}