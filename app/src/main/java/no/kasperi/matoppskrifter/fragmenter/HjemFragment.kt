package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.adapters.KategorierAdapter
import no.kasperi.matoppskrifter.adapters.MestPopulareAdapter
import no.kasperi.matoppskrifter.adapters.OnItemClick
import no.kasperi.matoppskrifter.adapters.OnLongItemClick
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.aktiviteter.OppskriftDetaljerActivity
import no.kasperi.matoppskrifter.databinding.FragmentHjemBinding
import no.kasperi.matoppskrifter.fragmenter.bunnDialog.OppskriftBunnDialogFragment
import no.kasperi.matoppskrifter.pojo.*
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import no.kasperi.matoppskrifter.viewModel.DetaljerViewModel
import no.kasperi.matoppskrifter.viewModel.HjemViewModel
import no.kasperi.matoppskrifter.viewModel.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HjemFragment : Fragment() {
    private lateinit var meal: TilfeldigOppskriftRespons
    private lateinit var detailMvvm: DetaljerViewModel
    private var randomMealId = "no.kasperi.matoppskrifter.fragmenter.randomMealId"
    companion object {
        const val OPPSKRIFT_ID = "no.kasperi.matoppskrifter.fragmenter.idMeal"
        const val OPPSKRIFT_NAVN = "no.kasperi.matoppskrifter.fragmenter.strMeal"
        const val OPPSKRIFT_BILDE = "no.kasperi.matoppskrifter.fragmenter.thumbMeal"
        const val CATEGORY_NAME = "no.kasperi.matoppskrifter.fragmenter.categoryName"
        const val MEAL_AREA = "no.kasperi.matoppskrifter.fragmenter.strArea"
        const val MEAL_NAME = "no.kasperi.matoppskrifter.fragmenter.strMeal"
    }

    private lateinit var myAdapter: KategorierAdapter
    private lateinit var mostPopularFoodAdapter: MestPopulareAdapter
    lateinit var binding: FragmentHjemBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailMvvm = ViewModelProvider(this)[DetaljerViewModel::class.java]
        binding = FragmentHjemBinding.inflate(layoutInflater)
        myAdapter = KategorierAdapter()
        mostPopularFoodAdapter = MestPopulareAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainFragMVVM = ViewModelProvider(this)[HjemViewModel::class.java]
        showLoadingCase()


        prepareCategoryRecyclerView()
        preparePopularMeals()
        onRndomMealClick()
        onRandomLongClick()


        mainFragMVVM.observeMealByCategory().observe(viewLifecycleOwner, object : Observer<OppskriftRespons> {
            override fun onChanged(t: OppskriftRespons?) {
                val meals = t!!.meals
                setMealsByCategoryAdapter(meals)
                cancelLoadingCase()
            }
        })

        mainFragMVVM.observeCategories().observe(viewLifecycleOwner, object :
            Observer<KategoriRespons> {
            override fun onChanged(t: KategoriRespons?) {
                val categories = t!!.categories
                setCategoryAdapter(categories)

            }
        })


        mainFragMVVM.observeRandomMeal().observe(viewLifecycleOwner, object :
           Observer<TilfeldigOppskriftRespons> {
            override fun onChanged(t: TilfeldigOppskriftRespons?) {
                val mealImage = view.findViewById<ImageView>(R.id.img_random_oppskrift)
                val imageUrl = t!!.meals[0].strMealThumb
                randomMealId = t.meals[0].idMeal
                Glide.with(this@HjemFragment)
                    .load(imageUrl)
                    .into(mealImage)
                meal = t
            }

       })

        mostPopularFoodAdapter.setOnClickListener(object : OnItemClick {
            override fun onItemClick(meal: Meal) {
                val intent = Intent(activity, OppskriftDetaljerActivity::class.java)
                intent.putExtra(OPPSKRIFT_ID, meal.idMeal)
                intent.putExtra(OPPSKRIFT_NAVN, meal.strMeal)
                intent.putExtra(OPPSKRIFT_BILDE, meal.strMealThumb)
                startActivity(intent)
            }

        })

        myAdapter.onItemClicked(object : KategorierAdapter.OnItemCategoryClicked {
            override fun onClickListener(kategori: Kategori) {
                val intent = Intent(activity, OppskriftActivity::class.java)
                intent.putExtra(CATEGORY_NAME, kategori.strCategory)
                startActivity(intent)
            }

        })

        mostPopularFoodAdapter.setOnLongCLickListener(object : OnLongItemClick {
            override fun onItemLongClick(meal: Meal) {
                detailMvvm.getMealByIdBottomSheet(meal.idMeal)
            }

        })

        detailMvvm.observeMealBottomSheet()
            .observe(viewLifecycleOwner, object : Observer<List<MealDetail>> {
                override fun onChanged(t: List<MealDetail>?) {
                    val bottomSheetFragment = OppskriftBunnDialogFragment()
                    val b = Bundle()
                    b.putString(CATEGORY_NAME, t!![0].strCategory)
                    b.putString(MEAL_AREA, t[0].strArea)
                    b.putString(MEAL_NAME, t[0].strMeal)
                    b.putString(OPPSKRIFT_BILDE, t[0].strMealThumb)
                    b.putString(OPPSKRIFT_ID, t[0].idMeal)

                    bottomSheetFragment.arguments = b

                    bottomSheetFragment.show(childFragmentManager, "BottomSheetDialog")
                }

            })


        // on search icon click
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_hjemFragment_to_sokFragment)
        }
    }


    private fun onRndomMealClick() {
        binding.randomOppskrift.setOnClickListener {
            val temp = meal.meals[0]
            val intent = Intent(activity, OppskriftDetaljerActivity::class.java)
            intent.putExtra(OPPSKRIFT_ID, temp.idMeal)
            intent.putExtra(OPPSKRIFT_NAVN, temp.strMeal)
            intent.putExtra(OPPSKRIFT_BILDE, temp.strMealThumb)
            startActivity(intent)
        }

    }

    private fun onRandomLongClick() {

        binding.randomOppskrift.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                detailMvvm.getMealByIdBottomSheet(randomMealId)
                return true
            }

        })
    }

    private fun showLoadingCase() {
        binding.apply {
            linearHeader.visibility = View.INVISIBLE
            tvSubheader.visibility = View.INVISIBLE
            randomOppskrift.visibility = View.INVISIBLE
            tvPopRetter.visibility = View.INVISIBLE
            recSePopRetter.visibility = View.INVISIBLE
            tvKategori.visibility = View.INVISIBLE
            categoryCard.visibility = View.INVISIBLE
            rootHjem.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        }
    }

    private fun cancelLoadingCase() {
        binding.apply {
            linearHeader.visibility = View.VISIBLE
            tvSubheader.visibility = View.VISIBLE
            randomOppskrift.visibility = View.VISIBLE
            tvPopRetter.visibility = View.VISIBLE
            recSePopRetter.visibility = View.VISIBLE
            tvKategori.visibility = View.VISIBLE
            categoryCard.visibility = View.VISIBLE
            rootHjem.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        }
    }

    private fun setMealsByCategoryAdapter(meals: List<Meal>) {
        mostPopularFoodAdapter.setMealList(meals)
    }

    private fun setCategoryAdapter(categories: List<Kategori>) {
        myAdapter.setKategoriListe(categories)
    }

    private fun prepareCategoryRecyclerView() {
        binding.recyclerViewKategorier.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun preparePopularMeals() {
        binding.recSePopRetter.apply {
            adapter = mostPopularFoodAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        }
    }



}