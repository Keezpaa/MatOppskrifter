package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.adapters.KategorierAdapter
import no.kasperi.matoppskrifter.adapters.MestPopulareAdapter
import no.kasperi.matoppskrifter.aktiviteter.KategoriActivity
import no.kasperi.matoppskrifter.aktiviteter.MainActivity
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.databinding.FragmentHjemBinding
import no.kasperi.matoppskrifter.fragmenter.bunnDialog.OppskriftBunnDialogFragment
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.OppskriftFraKategori
import no.kasperi.matoppskrifter.viewModel.HjemViewModel


class HjemFragment : Fragment() {

    private lateinit var binding:FragmentHjemBinding
    private lateinit var viewModel:HjemViewModel
    private lateinit var randomOppskrift:Meal
    private lateinit var populareRetterAdapter:MestPopulareAdapter
    private lateinit var kategorierAdapter:KategorierAdapter

    companion object {
        const val OPPSKRIFT_ID = "no.kasperi.matoppskrifter.fragmenter.idOppskrift"
        const val OPPSKRIFT_NAVN = "no.kasperi.matoppskrifter.fragmenter.navnOppskrift"
        const val OPPSKRIFT_BILDE = "no.kasperi.matoppskrifter.fragmenter.bildeOppskrift"
        const val KATEGORI_NAVN = "no.kasperi.matoppskrifter.fragmenter.kategoriNavn"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        populareRetterAdapter = MestPopulareAdapter()
        kategorierAdapter = KategorierAdapter()
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
        prepareKategorierRecyclerView()

        viewModel.hentTilfeldigOppskrift()
        observeTilfeldigOppskrift()
        onTilfeldigOppskriftClick()

        viewModel.hentPopulareRetter()
        observePopulareRetterLiveData()
        onPopularItemClick()

        viewModel.hentKategorier()
        observeKategorierLiveData()
        onKategoriClick()

        onPopularItemLongClick()
        onSokIkonClick()
    }

    private fun onSokIkonClick() {
        binding.imgSearch.setOnClickListener{
            findNavController().navigate(R.id.action_hjemFragment_to_sokFragment)
        }
    }

    private fun onPopularItemLongClick() {
        populareRetterAdapter.onLongItemClick = { oppskrift ->
            val oppskriftBunnDialogFragment = OppskriftBunnDialogFragment.newInstance(oppskrift.idMeal)
            oppskriftBunnDialogFragment.show(childFragmentManager,"Oppskriftinfo ")
        }
    }

    private fun onKategoriClick() {
        kategorierAdapter.onItemClick = { kategori ->
            val intent = Intent(activity, KategoriActivity::class.java)
            intent.putExtra(KATEGORI_NAVN, kategori.strCategory)
            startActivity(intent)

        }
    }

    private fun prepareKategorierRecyclerView() {
        binding.recyclerViewKategorier.apply {
            adapter = kategorierAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)

        }
    }

    private fun observeKategorierLiveData() {
        viewModel.observeKategorierLiveData().observe(viewLifecycleOwner) { categories ->
            kategorierAdapter.setKategoriListe(categories)
        }
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
        viewModel.observePopulareRetterLiveData().observe(viewLifecycleOwner)
        { oppskriftListe ->
            populareRetterAdapter.setOppskrifter(oppskriftListe = oppskriftListe as ArrayList<OppskriftFraKategori>)
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
            viewModel.observeTilfeldigOppskriftLiveData().observe(viewLifecycleOwner)
             { oppskrift ->
                Glide.with(this@HjemFragment)
                    .load(oppskrift!!.strMealThumb)
                    .into(binding.imgRandomOppskrift)

                this.randomOppskrift = oppskrift
            }
    }
}