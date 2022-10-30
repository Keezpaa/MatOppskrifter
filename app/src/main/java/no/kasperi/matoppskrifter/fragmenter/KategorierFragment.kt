package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import no.kasperi.matoppskrifter.adapters.KategorierAdapter
import no.kasperi.matoppskrifter.adapters.MestPopulareAdapter
import no.kasperi.matoppskrifter.aktiviteter.KategoriActivity
import no.kasperi.matoppskrifter.aktiviteter.MainActivity
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.databinding.FragmentKategorierBinding
import no.kasperi.matoppskrifter.viewModel.HjemViewModel


class KategorierFragment : Fragment() {
    private lateinit var binding:FragmentKategorierBinding
    private lateinit var kategorierAdapter:KategorierAdapter
    private lateinit var viewModel:HjemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        kategorierAdapter = KategorierAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKategorierBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observerKategorier()
        viewModel.hentKategorier()

        onKategoriClick()
    }



    private fun onKategoriClick() {
        kategorierAdapter.onItemClick = { kategori ->
            val intent = Intent(activity, KategoriActivity::class.java)
            intent.putExtra(HjemFragment.KATEGORI_NAVN, kategori.strCategory)
            startActivity(intent)

        }
    }

    private fun observerKategorier() {
        viewModel.observeKategorierLiveData().observe(viewLifecycleOwner, Observer{ kategorier ->
            kategorierAdapter.setKategoriListe(kategorier)

        })
    }

    private fun prepareRecyclerView() {
        kategorierAdapter = KategorierAdapter()
        binding.recKategorier.apply{
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL,false)
            adapter = kategorierAdapter
        }
    }
}