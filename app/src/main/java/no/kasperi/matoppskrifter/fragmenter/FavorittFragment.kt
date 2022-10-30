package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.adapters.FavorittAdapter
import no.kasperi.matoppskrifter.aktiviteter.MainActivity
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.databinding.FragmentFavorittBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.viewModel.HjemViewModel


class FavorittFragment : Fragment() {
    private lateinit var binding:FragmentFavorittBinding
    private lateinit var viewModel:HjemViewModel
    private lateinit var favorittAdapter:FavorittAdapter
    lateinit var recView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        favorittAdapter = FavorittAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavorittBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onFavorittOppskriftClick()
        prepareRecyclerView(view)
        observerFavoritter()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favorittOppskrift = favorittAdapter.differ.currentList[position]
                viewModel.slettOppskrift(favorittOppskrift)
                visSlettSnackbar(favorittOppskrift)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recView)
    }

    private fun visSlettSnackbar(favorittOppskrift: Meal){
        Snackbar.make(requireView(), "Oppskrift ble slettet", Snackbar.LENGTH_LONG).apply {
            setAction("Angre",View.OnClickListener {
                viewModel.insertOppskrift(favorittOppskrift)
            }).show()
        }
    }

    private fun onFavorittOppskriftClick() {
        favorittAdapter.setOnFavorittClickListener(object : FavorittAdapter.OnFavorittClickListener{
            override fun onFavorittClick(oppskrift: Meal) {
                val intent = Intent(context, OppskriftActivity::class.java)
                intent.putExtra(OPPSKRIFT_ID,oppskrift.idMeal)
                intent.putExtra(OPPSKRIFT_NAVN, oppskrift.strMeal)
                intent.putExtra(OPPSKRIFT_BILDE, oppskrift.strMealThumb)
                startActivity(intent)
            }
        })
    }

    private fun prepareRecyclerView(v:View) {
        recView =v.findViewById<RecyclerView>(R.id.rec_favoritter)
        recView.adapter = favorittAdapter
        recView.layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
    }
    private fun observerFavoritter() {
        viewModel.observerFavorittOppskrifterLiveData().observe(requireActivity(), Observer{ oppskrifter ->
             favorittAdapter.differ.submitList(oppskrifter)
        })
    }

}