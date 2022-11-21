package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.adapters.FavorittOppskriftAdapter
import no.kasperi.matoppskrifter.aktiviteter.OppskriftDetaljerActivity
import no.kasperi.matoppskrifter.databinding.FragmentFavorittBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.KATEGORI_NAVN
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_STED
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_TITTEL
import no.kasperi.matoppskrifter.fragmenter.bunnDialog.OppskriftBunnDialogFragment
import no.kasperi.matoppskrifter.pojo.MealDB
import no.kasperi.matoppskrifter.pojo.MealDetail
import no.kasperi.matoppskrifter.viewModel.DetaljerViewModel


class FavorittFragment : Fragment() {
    lateinit var recView:RecyclerView
    lateinit var fBinding:FragmentFavorittBinding
    private lateinit var favorittOppskriftAdapter: FavorittOppskriftAdapter
    /* MVVM = ModelView ViewModel */
    private lateinit var detaljerMVVM: DetaljerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favorittOppskriftAdapter = FavorittOppskriftAdapter()
        detaljerMVVM = ViewModelProvider(this)[DetaljerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fBinding = FragmentFavorittBinding.inflate(inflater,container,false)
        return fBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView(view)
        onFavorittOppskriftClick()
        onFavorittLongOppskriftClick()
        observerBunnDialog()

        detaljerMVVM.observerLagretOppskrift().observe(viewLifecycleOwner,object : Observer<List<MealDB>>{
            override fun onChanged(t: List<MealDB>?) {
                favorittOppskriftAdapter.setFavorittOppskriftListe(t!!)
                if(t.isEmpty())
                    fBinding.tvFavTom.visibility = View.VISIBLE

                else
                    fBinding.tvFavTom.visibility = View.GONE

            }
        })
        /* Swipefunksjon for sletting av matrett i favorittsiden
        * Til høyre eller venstre for sletting av oppskrift, og angrefunksjon i Snackbar */
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
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
                val favorittOppskrift = favorittOppskriftAdapter.hentOppskriftEtterPosisjon(position)
                detaljerMVVM.slettOppskrift(favorittOppskrift)
                visSletteSnackBar(favorittOppskrift)
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recView)

    }

    private fun visSletteSnackBar(favorittOppskrift:MealDB) {
        Snackbar.make(requireView(),"Favorittoppskrift fjernet!",Snackbar.LENGTH_LONG).apply {
            setAction("ANGRE",View.OnClickListener {
                detaljerMVVM.leggTilOppskrift(favorittOppskrift)
            }).show()
        }
    }

    private fun observerBunnDialog() {
        detaljerMVVM.observerOppskriftBunnDialog().observe(viewLifecycleOwner,object : Observer<List<MealDetail>>{
            override fun onChanged(t: List<MealDetail>?) {
                val bunnDialog = OppskriftBunnDialogFragment()
                val b = Bundle()
                b.putString(KATEGORI_NAVN,t!![0].strCategory)
                b.putString(OPPSKRIFT_STED,t[0].strArea)
                b.putString(OPPSKRIFT_TITTEL,t[0].strMeal)
                b.putString(OPPSKRIFT_BILDE,t[0].strMealThumb)
                b.putString(OPPSKRIFT_ID,t[0].idMeal)
                bunnDialog.arguments = b
                bunnDialog.show(childFragmentManager,"Favoritt bunndialog")
            }

        })
    }

    private fun prepareRecyclerView(v:View) {
        recView =v.findViewById<RecyclerView>(R.id.rec_favoritter)
        recView.adapter = favorittOppskriftAdapter
        recView.layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
    }

    private fun onFavorittOppskriftClick(){
        favorittOppskriftAdapter.setOnFavorittOppskriftClickListener(object : FavorittOppskriftAdapter.OnFavorittClickListener{
            override fun onFavorittClick(meal: MealDB) {
                val intent = Intent(context, OppskriftDetaljerActivity::class.java)
                intent.putExtra(OPPSKRIFT_ID,meal.mealId.toString())
                intent.putExtra(OPPSKRIFT_NAVN,meal.mealName)
                intent.putExtra(OPPSKRIFT_BILDE,meal.mealThumb)
                startActivity(intent)
            }

        })
    }

    private fun onFavorittLongOppskriftClick() {
        favorittOppskriftAdapter.setOnFavorittLongClickListener(object : FavorittOppskriftAdapter.OnFavorittLongClickListener{
            override fun onFavorittLongCLick(meal: MealDB) {
                detaljerMVVM.hentOppskriftEtterIdBunnDialog(meal.mealId.toString())
            }

        })
    }


}