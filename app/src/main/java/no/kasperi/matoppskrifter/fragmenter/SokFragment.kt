package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.kasperi.matoppskrifter.adapters.KategorierAdapter
import no.kasperi.matoppskrifter.adapters.OppskriftAdapter
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.databinding.FragmentSokBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.viewModel.SokViewModel


class SokFragment : Fragment() {
    private lateinit var binding:FragmentSokBinding
    private lateinit var sokViewModel: SokViewModel
    private lateinit var sokRecyclerViewAdapter:OppskriftAdapter
    private lateinit var oppskriftAdapter: OppskriftAdapter
    private var oppskriftId = "no.kasperi.matoppskrifter.fragmenter.idOppskrift"
    private var oppskriftNavn = "no.kasperi.matoppskrifter.fragmenter.navnOppskrift"
    private var oppskriftBilde = "no.kasperi.matoppskrifter.fragmenter.bildeOppskrift"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     //   sokViewModel = (activity as MainActivity).sokViewModel
        sokViewModel = ViewModelProvider(this)[SokViewModel::class.java]
        sokRecyclerViewAdapter = OppskriftAdapter()
        oppskriftAdapter = OppskriftAdapter()
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSokBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observerSokLiveData()
        binding.imgSok.setOnClickListener{ sokEtterOppskrifter() }

        // Kodenbiten under gjør at søkeresultatene oppdateres samtidig som brukeren skriver inn en oppskrift,
        // uten at du trenger å klikke på søkeikonet
        var sokJobb: Job? = null
        binding.edtSokBox.addTextChangedListener { searchQuery ->
            sokJobb?.cancel()
            sokJobb = lifecycleScope.launch{
                delay(500)
                sokViewModel.sokEtterOppskrifter(searchQuery.toString())
            }
        }

    }

    private fun sokEtterOppskrifter() {
        val searchQuery = binding.edtSokBox.text.toString()
        if(searchQuery.isNotEmpty()){
            sokViewModel.sokEtterOppskrifter(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        sokRecyclerViewAdapter = OppskriftAdapter()
        binding.recSokOppskrift.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = sokRecyclerViewAdapter
        }
    }



    private fun observerSokLiveData() {
        sokViewModel.observerSoktLiveData()
            .observe(viewLifecycleOwner, Observer { oppskriftListe ->
                sokRecyclerViewAdapter.differ.submitList(oppskriftListe)

            })
    }


}