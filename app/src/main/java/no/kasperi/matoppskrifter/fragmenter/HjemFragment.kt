package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.adapters.MestPopulareAdapter
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.databinding.FragmentHjemBinding
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.OppskriftKategori
import no.kasperi.matoppskrifter.viewModel.HjemViewModel


class HjemFragment : Fragment() {

    private lateinit var binding:FragmentHjemBinding
    private lateinit var hjemMvvm:HjemViewModel
    private lateinit var randomOppskrift:Meal
    private lateinit var populareRetterAdapter:MestPopulareAdapter

    companion object {
        const val OPPSKRIFT_ID = "no.kasperi.matoppskrifter.fragmenter.idOppskrift"
        const val OPPSKRIFT_NAVN = "no.kasperi.matoppskrifter.fragmenter.navnOppskrift"
        const val OPPSKRIFT_BILDE = "no.kasperi.matoppskrifter.fragmenter.bildeOppskrift"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hjemMvvm = ViewModelProvider(this)[HjemViewModel::class.java]

        populareRetterAdapter = MestPopulareAdapter()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHjemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopulareRetterRecyclerView()


        hjemMvvm.hentTilfeldigOppskrift()
        observeTilfeldigOppskrift()
        onTilfeldigOppskriftClick()

        hjemMvvm.hentPopulareRetter()
        observePopulareRetterLiveData()
        onPopularItemClick()
    }

    private fun onPopularItemClick() {
        populareRetterAdapter.onItemClick = { oppskrift ->
            val intent = Intent(activity, OppskriftActivity::class.java)
            intent.putExtra(OPPSKRIFT_ID, oppskrift.idMeal)
            intent.putExtra(OPPSKRIFT_NAVN, oppskrift.strMeal)
            intent.putExtra(OPPSKRIFT_BILDE, oppskrift.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopulareRetterRecyclerView() {
        binding.recSePopRetter.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = populareRetterAdapter
        }
    }

    private fun observePopulareRetterLiveData() {
        hjemMvvm.observePopulareRetterLiveData().observe(viewLifecycleOwner)
        { oppskriftListe ->
            populareRetterAdapter.setOppskrifter(oppskriftListe = oppskriftListe as ArrayList<OppskriftKategori>)
        }
    }

    private fun onTilfeldigOppskriftClick() {
        binding.randomOppskrift.setOnClickListener {
            val intent = Intent(activity, OppskriftActivity::class.java)
            intent.putExtra(OPPSKRIFT_ID, randomOppskrift.idMeal)
            intent.putExtra(OPPSKRIFT_NAVN, randomOppskrift.strMeal)
            intent.putExtra(OPPSKRIFT_BILDE, randomOppskrift.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeTilfeldigOppskrift() {
            hjemMvvm.observeTilfeldigOppskriftLiveData().observe(viewLifecycleOwner)
             { oppskrift ->
                Glide.with(this@HjemFragment)
                    .load(oppskrift!!.strMealThumb)
                    .into(binding.imgRandomOppskrift)

                this.randomOppskrift = oppskrift
            }
    }
}