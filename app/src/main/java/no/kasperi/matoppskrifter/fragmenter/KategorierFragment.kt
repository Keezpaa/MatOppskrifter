package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.adapters.KategorierAdapter
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.databinding.FragmentKategorierBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.KATEGORI_NAVN
import no.kasperi.matoppskrifter.pojo.Kategori
import no.kasperi.matoppskrifter.viewModel.KategoriViewModel


class KategorierFragment : Fragment(R.layout.fragment_kategorier) {
    private lateinit var binding:FragmentKategorierBinding
    private lateinit var myAdapter:KategorierAdapter
    private lateinit var categoryMvvm: KategoriViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = KategorierAdapter()
        categoryMvvm = ViewModelProvider(this)[KategoriViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKategorierBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeCategories()
        onCategoryClick()
    }

    private fun onCategoryClick() {
        myAdapter.onItemClicked(object : KategorierAdapter.OnItemKategoriClicked {
            override fun onClickListener(kategori: Kategori) {
                val intent = Intent(context, OppskriftActivity::class.java)
                intent.putExtra(KATEGORI_NAVN,kategori.strCategory)
                startActivity(intent)
            }
        })
    }

    private fun observeCategories() {
        categoryMvvm.observerKategorier().observe(viewLifecycleOwner
        ) { t -> myAdapter.setKategoriListe(t!!) }
    }

    private fun prepareRecyclerView() {
        binding.recKategorier.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
        }
    }


}