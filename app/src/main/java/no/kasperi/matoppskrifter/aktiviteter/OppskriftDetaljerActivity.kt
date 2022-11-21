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
    /* MVVM = ModeView ViewModel */
    private lateinit var detaljerMVVM: DetaljerViewModel
    private var oppskriftId = "no.kasperi.matoppskrifter.fragmenter.idMeal"
    private var oppskriftNavn = "no.kasperi.matoppskrifter.fragmenter.strMeal"
    private var oppskriftBilde = "no.kasperi.matoppskrifter.fragmenter.thumbMeal"
    private var ytUrl = "no.kasperi.matoppskrifter.fragmenter.ytUrl"
    private lateinit var dtOppskrift: MealDetail


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detaljerMVVM = ViewModelProvider(this)[DetaljerViewModel::class.java]
        binding = ActivityOppskriftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        visLasting()
        hentOppskriftInfoFraIntent()
        settOppViewMedOppskriftInfo()
        settFavorittKnappeStatus()

        detaljerMVVM.hentOppskriftEtterId(oppskriftId)

        detaljerMVVM.observerOppskriftDetaljer().observe(this, object : Observer<List<MealDetail>> {
            override fun onChanged(t: List<MealDetail>?) {
                settTextIViews(t!![0])
                stoppLasting()
            }

        })

        binding.imgYt.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ytUrl)))
        }


        binding.btnFavoritt.setOnClickListener {
            if(erOppskriftLagretIDatabasen()){
                slettOppskrift()
                binding.btnFavoritt.setImageResource(R.drawable.ic_hjerte)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Oppskrift fjernet fra favoritter!",
                    Snackbar.LENGTH_SHORT).show()
            }else{
                lagreOppskrift()
                binding.btnFavoritt.setImageResource(R.drawable.ic_fylt_hjerte)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Oppskrift lagret!",
                    Snackbar.LENGTH_SHORT).show()
            }
        }

    }



    private fun slettOppskrift() {
        detaljerMVVM.slettOppskriftEtterId(oppskriftId)
    }

    private fun settFavorittKnappeStatus() {
        if(erOppskriftLagretIDatabasen()){
            binding.btnFavoritt.setImageResource(R.drawable.ic_fylt_hjerte)
        }else{
            binding.btnFavoritt.setImageResource(R.drawable.ic_hjerte)
        }
    }

    private fun erOppskriftLagretIDatabasen(): Boolean {
        return detaljerMVVM.erOppskriftLagretIDatabasen(oppskriftId)
    }

    private fun lagreOppskrift() {
        val meal = MealDB(dtOppskrift.idMeal.toInt(),
            dtOppskrift.strMeal,
            dtOppskrift.strArea,
            dtOppskrift.strCategory,
            dtOppskrift.strInstructions,
            dtOppskrift.strMealThumb,
            dtOppskrift.strYoutube)

        detaljerMVVM.leggTilOppskrift(meal)
    }

    private fun visLasting() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnFavoritt.visibility = View.GONE
        binding.imgYt.visibility = View.INVISIBLE
    }


    private fun stoppLasting() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnFavoritt.visibility = View.VISIBLE
        binding.imgYt.visibility = View.VISIBLE
    }

    private fun settTextIViews(meal: MealDetail) {
        this.dtOppskrift = meal
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


    private fun settOppViewMedOppskriftInfo() {
        binding.apply {
            collapsingToolbar.title = oppskriftNavn
            Glide.with(applicationContext)
                .load(oppskriftBilde)
                .into(imgOppskriftDetaljer)
        }

    }

    private fun hentOppskriftInfoFraIntent() {
        val tempIntent = intent

        this.oppskriftId = tempIntent.getStringExtra(OPPSKRIFT_ID)!!
        this.oppskriftNavn = tempIntent.getStringExtra(OPPSKRIFT_NAVN)!!
        this.oppskriftBilde = tempIntent.getStringExtra(OPPSKRIFT_BILDE)!!
    }

}