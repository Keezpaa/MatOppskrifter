package no.kasperi.matoppskrifter.aktiviteter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import no.kasperi.matoppskrifter.adapters.OppskriftAdapter
import no.kasperi.matoppskrifter.adapters.SetOnOppskriftClickListener
import no.kasperi.matoppskrifter.databinding.ActivityKategoriBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.KATEGORI_NAVN
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.viewModel.OppskriftViewModel

class OppskriftActivity : AppCompatActivity() {

    private lateinit var oppskriftViewModel: OppskriftViewModel
    private lateinit var binding: ActivityKategoriBinding
    private lateinit var oppskriftAdapter: OppskriftAdapter
    private var kategoriNavn = "no.kasperi.matoppskrifter.fragmenter.categoryName"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        forberedRecyclerView()
        oppskriftViewModel = ViewModelProvider(this)[OppskriftViewModel::class.java]
        oppskriftViewModel.hentOppskriftEtterKategori(hentKategori())
        oppskriftViewModel.observerOppskrift().observe(this, object : Observer<List<Meal>> {

            override fun onChanged(t: List<Meal>?) {
                if(t==null){
                    Toast.makeText(applicationContext, "Ingen oppskrifter i denne kategorien", Toast.LENGTH_SHORT).show()
                }else {
                    oppskriftAdapter.setKategoriListe(t!!)
                    binding.tvKategoriAntall.text = kategoriNavn  + " : " + t.size.toString()

                }
            }
        })

        oppskriftAdapter.setOnOppskriftClickListener(object : SetOnOppskriftClickListener {
            override fun setOnClickListener(meal: Meal) {
                val intent = Intent(applicationContext, OppskriftDetaljerActivity::class.java)
                intent.putExtra(OPPSKRIFT_ID, meal.idMeal)
                intent.putExtra(OPPSKRIFT_NAVN, meal.strMeal)
                intent.putExtra(OPPSKRIFT_BILDE, meal.strMealThumb)
                startActivity(intent)
            }
        })
    }

    private fun hentKategori(): String {
        val tempIntent = intent
        val x = intent.getStringExtra(KATEGORI_NAVN)!!
        kategoriNavn = x
        return x
    }

    private fun forberedRecyclerView() {
        oppskriftAdapter = OppskriftAdapter()
        binding.oppskriftRecyclerview.apply {
            adapter = oppskriftAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }
}