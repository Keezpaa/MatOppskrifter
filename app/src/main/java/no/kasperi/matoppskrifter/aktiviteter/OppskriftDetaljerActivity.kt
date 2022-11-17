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
                    "Oppskrift fjernet fra favoritter!",
                    Snackbar.LENGTH_SHORT).show()
            }else{
                saveMeal()
                binding.btnFavoritt.setImageResource(R.drawable.ic_fylt_hjerte)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Oppskrift lagret!",
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
            tvIngredienser.text = "Ingredienser: "
            tvIngredienser.text = "${meal.strIngredient1}: ${meal.strMeasure1}\n" +
                    "${meal.strIngredient2}: ${meal.strMeasure2}\n" +
                    "${meal.strIngredient3}: ${meal.strMeasure3}\n" +
                    "${meal.strIngredient4}: ${meal.strMeasure4}\n" +
                    "${meal.strIngredient5}: ${meal.strMeasure5}\n" +
                    "${meal.strIngredient6}: ${meal.strMeasure6}\n" +
                    "${meal.strIngredient7}: ${meal.strMeasure7}\n" +
                    "${meal.strIngredient8}: ${meal.strMeasure8}\n" +
                    "${meal.strIngredient9}: ${meal.strMeasure9}\n" +
                    "${meal.strIngredient10}: ${meal.strMeasure10}\n" +
                    "${meal.strIngredient11}: ${meal.strMeasure11}\n" +
                    "${meal.strIngredient12}: ${meal.strMeasure12}\n" +
                    "${meal.strIngredient13}: ${meal.strMeasure13}\n" +
                    "${meal.strIngredient14}: ${meal.strMeasure14}\n" +
                    "${meal.strIngredient15}: ${meal.strMeasure15}\n" +
                    "${meal.strIngredient16}: ${meal.strMeasure16}\n" +
                    "${meal.strIngredient17}: ${meal.strMeasure17}\n" +
                    "${meal.strIngredient18}: ${meal.strMeasure18}\n" +
                    "${meal.strIngredient19}: ${meal.strMeasure19}\n" +
                    "${meal.strIngredient20}: ${meal.strMeasure20}"
            tvInstruksjoner.text = "Instruksjoner: "
            tvInstruksjonerText.text = meal.strInstructions
            tvSted.visibility = View.VISIBLE
            tvKategori.visibility = View.VISIBLE
            tvKategori.text = "Kategori: ${meal.strCategory}"
            tvSted.text = meal.strArea
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