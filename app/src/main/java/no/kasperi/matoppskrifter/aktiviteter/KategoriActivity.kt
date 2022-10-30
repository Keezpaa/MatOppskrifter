package no.kasperi.matoppskrifter.aktiviteter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.adapters.OppskriftKategoriAdapter
import no.kasperi.matoppskrifter.databinding.ActivityKategoriBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment
import no.kasperi.matoppskrifter.viewModel.KategoriViewModel

class KategoriActivity : AppCompatActivity() {

    lateinit var binding : ActivityKategoriBinding
    lateinit var kategoriViewModel:KategoriViewModel
    lateinit var oppskriftKategoriAdapter:OppskriftKategoriAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        kategoriViewModel = ViewModelProvider(this)[KategoriViewModel::class.java]

        kategoriViewModel.hentOppskriftFraKategori(intent.getStringExtra(HjemFragment.KATEGORI_NAVN)!!)

        kategoriViewModel.observeOppskrifterLiveData().observe(this, Observer { oppskriftListe ->
            binding.tvKategoriAntall.text = "${oppskriftListe.size.toString()} oppskrift(er)"
                oppskriftKategoriAdapter.setOppskriftListe(oppskriftListe)
            })
    }


    private fun prepareRecyclerView() {
        oppskriftKategoriAdapter = OppskriftKategoriAdapter()
        binding.recOppskrifter.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL, false)
            adapter = oppskriftKategoriAdapter
        }
    }
}