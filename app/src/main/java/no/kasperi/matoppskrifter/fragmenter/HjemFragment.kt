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
    private lateinit var oppskrift: TilfeldigOppskriftRespons
    /* MVVM = ModelView ViewModel */
    private lateinit var detaljerMVVM: DetaljerViewModel
    private var tilfeldigOppskriftId = "no.kasperi.matoppskrifter.fragmenter.randomMealId"
    private lateinit var kategoriAdapter: KategorierAdapter
    private lateinit var mestPopulareAdapter: MestPopulareAdapter
    lateinit var binding: FragmentHjemBinding

    companion object {
        const val OPPSKRIFT_ID = "no.kasperi.matoppskrifter.fragmenter.idMeal"
        const val OPPSKRIFT_NAVN = "no.kasperi.matoppskrifter.fragmenter.strMeal"
        const val OPPSKRIFT_BILDE = "no.kasperi.matoppskrifter.fragmenter.thumbMeal"
        const val KATEGORI_NAVN = "no.kasperi.matoppskrifter.fragmenter.categoryName"
        const val OPPSKRIFT_STED = "no.kasperi.matoppskrifter.fragmenter.strArea"
        const val OPPSKRIFT_TITTEL = "no.kasperi.matoppskrifter.fragmenter.strMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detaljerMVVM = ViewModelProvider(this)[DetaljerViewModel::class.java]
        binding = FragmentHjemBinding.inflate(layoutInflater)
        kategoriAdapter = KategorierAdapter()
        mestPopulareAdapter = MestPopulareAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* MVVM = ModelView ViewModel */
        val hjemMVVM = ViewModelProvider(this)[HjemViewModel::class.java]
        showLoadingCase()


        forberedKategorierRecyclerView()
        forberedPopulareOppskrifter()
        onRandomMealClick()
        onRandomLongClick()


        hjemMVVM.observerOppskriftEtterKategori().observe(viewLifecycleOwner, object : Observer<OppskriftRespons> {
            override fun onChanged(t: OppskriftRespons?) {
                val meals = t!!.meals
                setMealsByCategoryAdapter(meals)
                cancelLoadingCase()
            }
        })

        hjemMVVM.observerKategorier().observe(viewLifecycleOwner, object :
            Observer<KategoriRespons> {
            override fun onChanged(t: KategoriRespons?) {
                val categories = t!!.categories
                setCategoryAdapter(categories)

            }
        })


        hjemMVVM.observerTilfeldigOppskrift().observe(viewLifecycleOwner, object :
           Observer<TilfeldigOppskriftRespons> {
            override fun onChanged(t: TilfeldigOppskriftRespons?) {
                val mealImage = view.findViewById<ImageView>(R.id.img_random_oppskrift)
                val imageUrl = t!!.meals[0].strMealThumb
                tilfeldigOppskriftId = t.meals[0].idMeal
                Glide.with(this@HjemFragment)
                    .load(imageUrl)
                    .into(mealImage)
                oppskrift = t
            }

       })

        mestPopulareAdapter.setOnClickListener(object : OnItemClick {
            override fun onItemClick(meal: Meal) {
                val intent = Intent(activity, OppskriftDetaljerActivity::class.java)
                intent.putExtra(OPPSKRIFT_ID, meal.idMeal)
                intent.putExtra(OPPSKRIFT_NAVN, meal.strMeal)
                intent.putExtra(OPPSKRIFT_BILDE, meal.strMealThumb)
                startActivity(intent)
            }

        })

        kategoriAdapter.onItemClicked(object : KategorierAdapter.OnItemKategoriClicked {
            override fun onClickListener(kategori: Kategori) {
                val intent = Intent(activity, OppskriftActivity::class.java)
                intent.putExtra(KATEGORI_NAVN, kategori.strCategory)
                startActivity(intent)
            }

        })

        mestPopulareAdapter.setOnLongCLickListener(object : OnLongItemClick {
            override fun onItemLongClick(meal: Meal) {
                detaljerMVVM.hentOppskriftEtterIdBunnDialog(meal.idMeal)
            }

        })

        detaljerMVVM.observerOppskriftBunnDialog()
            .observe(viewLifecycleOwner, object : Observer<List<MealDetail>> {
                override fun onChanged(t: List<MealDetail>?) {
                    val bunnDialogFrag = OppskriftBunnDialogFragment()
                    val b = Bundle()
                    b.putString(KATEGORI_NAVN, t!![0].strCategory)
                    b.putString(OPPSKRIFT_STED, t[0].strArea)
                    b.putString(OPPSKRIFT_TITTEL, t[0].strMeal)
                    b.putString(OPPSKRIFT_BILDE, t[0].strMealThumb)
                    b.putString(OPPSKRIFT_ID, t[0].idMeal)

                    bunnDialogFrag.arguments = b

                    bunnDialogFrag.show(childFragmentManager, "Bunndialog")
                }

            })


        // Klikk på søkeikon på hjemside for søk av spesifik matoppskrift
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_hjemFragment_to_sokFragment)
        }
    }


    private fun onRandomMealClick() {
        binding.randomOppskrift.setOnClickListener {
            val temp = oppskrift.meals[0]
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
                detaljerMVVM.hentOppskriftEtterIdBunnDialog(tilfeldigOppskriftId)
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
        mestPopulareAdapter.setOppskriftListe(meals)
    }

    private fun setCategoryAdapter(categories: List<Kategori>) {
        kategoriAdapter.setKategoriListe(categories)
    }

    private fun forberedKategorierRecyclerView() {
        binding.recyclerViewKategorier.apply {
            adapter = kategoriAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun forberedPopulareOppskrifter() {
        binding.recSePopRetter.apply {
            adapter = mestPopulareAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        }
    }



}