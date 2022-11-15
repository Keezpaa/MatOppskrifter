package no.kasperi.matoppskrifter.aktiviteter

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.databinding.ActivityOppskriftBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.pojo.MealDB
import no.kasperi.matoppskrifter.pojo.MealDetail
import no.kasperi.matoppskrifter.viewModel.DetaljerViewModel

class OppskriftDetaljerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOppskriftBinding
    private lateinit var detailsMVVM: DetaljerViewModel
    private var mealId = "no.kasperi.matoppskrifter.fragmenter.idMeal"
    private var mealStr = "no.kasperi.matoppskrifter.fragmenter.strMeal"
    private var mealThumb = "no.kasperi.matoppskrifter.fragmenter.thumbMeal"
    private var ytUrl = "no.kasperi.matoppskrifter.fragmenter.ytUrl"
    private lateinit var dtMeal: MealDetail



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsMVVM = ViewModelProvider(this)[DetaljerViewModel::class.java]
        binding = ActivityOppskriftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading()

        getMealInfoFromIntent()
        setUpViewWithMealInformation()
        setFloatingButtonStatues()

        detailsMVVM.getMealById(mealId)

        detailsMVVM.observeMealDetail().observe(this, object : Observer<List<MealDetail>> {
            override fun onChanged(t: List<MealDetail>?) {
                setTextsInViews(t!![0])
                stopLoading()
            }

        })

        binding.imgYt.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ytUrl)))
        }


        binding.btnFavoritt.setOnClickListener {
            if(isMealSavedInDatabase()){
                deleteMeal()
                binding.btnFavoritt.setImageResource(R.drawable.ic_hjerte)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Meal was deleted",
                    Snackbar.LENGTH_SHORT).show()
            }else{
                saveMeal()
                binding.btnFavoritt.setImageResource(R.drawable.ic_fylt_hjerte)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Meal saved",
                    Snackbar.LENGTH_SHORT).show()
            }
        }

    }



    private fun deleteMeal() {
        detailsMVVM.deleteMealById(mealId)
    }

    private fun setFloatingButtonStatues() {
        if(isMealSavedInDatabase()){
            binding.btnFavoritt.setImageResource(R.drawable.ic_fylt_hjerte)
        }else{
            binding.btnFavoritt.setImageResource(R.drawable.ic_hjerte)
        }
    }

    private fun isMealSavedInDatabase(): Boolean {
        return detailsMVVM.isMealSavedInDatabase(mealId)
    }

    private fun saveMeal() {
        val meal = MealDB(dtMeal.idMeal.toInt(),
            dtMeal.strMeal,
            dtMeal.strArea,
            dtMeal.strCategory,
            dtMeal.strInstructions,
            dtMeal.strMealThumb,
            dtMeal.strYoutube)

        detailsMVVM.insertMeal(meal)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnFavoritt.visibility = View.GONE
        binding.imgYt.visibility = View.INVISIBLE
    }


    private fun stopLoading() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnFavoritt.visibility = View.VISIBLE

        binding.imgYt.visibility = View.VISIBLE

    }

    private fun setTextsInViews(meal: MealDetail) {
        this.dtMeal = meal
        ytUrl = meal.strYoutube
        binding.apply {
            tvInstruksjoner.text = "- Instructions : "
            tvInstruksjonerText.text = meal.strInstructions
            tvSted.visibility = View.VISIBLE
            tvKategori.visibility = View.VISIBLE
            tvSted.text = tvSted.text.toString() + meal.strArea
            tvKategori.text = tvKategori.text.toString() + meal.strCategory
            imgYt.visibility = View.VISIBLE
        }
    }


    private fun setUpViewWithMealInformation() {
        binding.apply {
            collapsingToolbar.title = mealStr
            Glide.with(applicationContext)
                .load(mealThumb)
                .into(imgOppskriftDetaljer)
        }

    }

    private fun getMealInfoFromIntent() {
        val tempIntent = intent

        this.mealId = tempIntent.getStringExtra(OPPSKRIFT_ID)!!
        this.mealStr = tempIntent.getStringExtra(OPPSKRIFT_NAVN)!!
        this.mealThumb = tempIntent.getStringExtra(OPPSKRIFT_BILDE)!!
    }

}