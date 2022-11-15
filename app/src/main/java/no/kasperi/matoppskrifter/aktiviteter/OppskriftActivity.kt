package no.kasperi.matoppskrifter.aktiviteter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import no.kasperi.matoppskrifter.adapters.OppskriftAdapter
import no.kasperi.matoppskrifter.adapters.SetOnMealClickListener
import no.kasperi.matoppskrifter.databinding.ActivityKategoriBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.CATEGORY_NAME
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.viewModel.OppskriftViewModel

class OppskriftActivity : AppCompatActivity() {
    private lateinit var mealActivityMvvm: OppskriftViewModel
    private lateinit var binding: ActivityKategoriBinding
    private lateinit var myAdapter: OppskriftAdapter
    private var categoryNme = "no.kasperi.matoppskrifter.fragmenter.categoryName"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mealActivityMvvm = ViewModelProvider(this)[OppskriftViewModel::class.java]
        prepareRecyclerView()
        mealActivityMvvm.getMealsByCategory(getCategory())
        mealActivityMvvm.observeMeal().observe(this, object : Observer<List<Meal>> {
            override fun onChanged(t: List<Meal>?) {
                if(t==null){
                    Toast.makeText(applicationContext, "No meals in this category", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }else {
                    myAdapter.setCategoryList(t!!)
                    binding.tvKategoriAntall.text = categoryNme + " : " + t.size.toString()

                }
            }
        })

        myAdapter.setOnMealClickListener(object : SetOnMealClickListener {
            override fun setOnClickListener(meal: Meal) {
                val intent = Intent(applicationContext, OppskriftDetaljerActivity::class.java)
                intent.putExtra(OPPSKRIFT_ID, meal.idMeal)
                intent.putExtra(OPPSKRIFT_NAVN, meal.strMeal)
                intent.putExtra(OPPSKRIFT_BILDE, meal.strMealThumb)
                startActivity(intent)
            }
        })
    }



    private fun getCategory(): String {
        val tempIntent = intent
        val x = intent.getStringExtra(CATEGORY_NAME)!!
        categoryNme = x
        return x
    }

    private fun prepareRecyclerView() {
        myAdapter = OppskriftAdapter()
        binding.oppskriftRecyclerview.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }
}