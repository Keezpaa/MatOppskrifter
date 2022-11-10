package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.adapters.FavorittOppskriftAdapter
import no.kasperi.matoppskrifter.adapters.OppskriftAdapter
import no.kasperi.matoppskrifter.aktiviteter.MainActivity
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.aktiviteter.OppskriftDetaljerActivity
import no.kasperi.matoppskrifter.databinding.FragmentFavorittBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.CATEGORY_NAME
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.MEAL_AREA
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.MEAL_NAME
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.fragmenter.bunnDialog.OppskriftBunnDialogFragment
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.MealDB
import no.kasperi.matoppskrifter.pojo.MealDetail
import no.kasperi.matoppskrifter.viewModel.DetaljerViewModel
import no.kasperi.matoppskrifter.viewModel.HjemViewModel
import no.kasperi.matoppskrifter.viewModel.OppskriftViewModel


class FavorittFragment : Fragment() {
    lateinit var recView:RecyclerView
    lateinit var fBinding:FragmentFavorittBinding
    private lateinit var myAdapter: FavorittOppskriftAdapter
    private lateinit var detailsMVVM: DetaljerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = FavorittOppskriftAdapter()
        detailsMVVM = ViewModelProvider(this)[DetaljerViewModel::class.java]
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
        onFavoriteMealClick()
        onFavoriteLongMealClick()
        observeBottomDialog()

        detailsMVVM.observeSaveMeal().observe(viewLifecycleOwner,object : Observer<List<MealDB>>{
            override fun onChanged(t: List<MealDB>?) {
                myAdapter.setFavoriteMealsList(t!!)
                if(t.isEmpty())
                    fBinding.tvFavTom.visibility = View.VISIBLE

                else
                    fBinding.tvFavTom.visibility = View.GONE

            }
        })

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
                val favoriteMeal = myAdapter.getMelaByPosition(position)
                detailsMVVM.deleteMeal(favoriteMeal)
                showDeleteSnackBar(favoriteMeal)
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recView)

    }

    private fun showDeleteSnackBar(favoriteMeal:MealDB) {
        Snackbar.make(requireView(),"Meal was deleted",Snackbar.LENGTH_LONG).apply {
            setAction("undo",View.OnClickListener {
                detailsMVVM.insertMeal(favoriteMeal)
            }).show()
        }
    }

    private fun observeBottomDialog() {
        detailsMVVM.observeMealBottomSheet().observe(viewLifecycleOwner,object : Observer<List<MealDetail>>{
            override fun onChanged(t: List<MealDetail>?) {
                val bottomDialog = OppskriftBunnDialogFragment()
                val b = Bundle()
                b.putString(CATEGORY_NAME,t!![0].strCategory)
                b.putString(MEAL_AREA,t[0].strArea)
                b.putString(MEAL_NAME,t[0].strMeal)
                b.putString(OPPSKRIFT_NAVN,t[0].strMealThumb)
                b.putString(OPPSKRIFT_ID,t[0].idMeal)
                bottomDialog.arguments = b
                bottomDialog.show(childFragmentManager,"Favorite bottom dialog")
            }

        })
    }

    private fun prepareRecyclerView(v:View) {
        recView =v.findViewById<RecyclerView>(R.id.rec_favoritter)
        recView.adapter = myAdapter
        recView.layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
    }

    private fun onFavoriteMealClick(){
        myAdapter.setOnFavoriteMealClickListener(object : FavorittOppskriftAdapter.OnFavoriteClickListener{
            override fun onFavoriteClick(meal: MealDB) {
                val intent = Intent(context, OppskriftDetaljerActivity::class.java)
                intent.putExtra(OPPSKRIFT_ID,meal.mealId.toString())
                intent.putExtra(OPPSKRIFT_NAVN,meal.mealName)
                intent.putExtra(OPPSKRIFT_BILDE,meal.mealThumb)
                startActivity(intent)
            }

        })
    }

    private fun onFavoriteLongMealClick() {
        myAdapter.setOnFavoriteLongClickListener(object : FavorittOppskriftAdapter.OnFavoriteLongClickListener{
            override fun onFavoriteLongCLick(meal: MealDB) {
                detailsMVVM.getMealByIdBottomSheet(meal.mealId.toString())
            }

        })
    }


}