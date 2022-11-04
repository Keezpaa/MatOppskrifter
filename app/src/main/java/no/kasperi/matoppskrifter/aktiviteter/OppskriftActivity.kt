package no.kasperi.matoppskrifter.aktiviteter

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.databinding.ActivityOppskriftBinding
import no.kasperi.matoppskrifter.db.OppskriftDB
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.viewModel.OppskriftViewModel
import no.kasperi.matoppskrifter.viewModel.OppskriftViewModelFactory

class OppskriftActivity : AppCompatActivity() {
    private lateinit var oppskriftId:String
    private lateinit var oppskriftNavn:String
    private lateinit var oppskriftBilde:String
    private lateinit var binding: ActivityOppskriftBinding
    private lateinit var oppskriftMvvm:OppskriftViewModel

    private lateinit var ytLink:String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOppskriftBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val oppskriftDB = OppskriftDB.hentInstance(this)
        val viewModelFactory = OppskriftViewModelFactory(oppskriftDB)
        oppskriftMvvm = ViewModelProvider(this, viewModelFactory)[OppskriftViewModel::class.java]

        hentOppskriftInformasjonFraIntent()
        setInfoIViews()

        loadingCase()
        oppskriftMvvm.hentOppskriftDetaljer(oppskriftId)
        observerOppskriftDetaljerLiveData()

        onYoutubeClick()
        onFavorittClick()
    }



    private fun onFavorittClick() {

        binding.btnFavoritt.setOnClickListener{
            oppskriftTilLagring?.let{
                oppskriftMvvm.insertOppskrift(it)
                    binding.btnFavoritt.setImageResource(R.drawable.ic_fylt_hjerte)
                    Toast.makeText(this,"Matrett lagret!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeClick() {
        binding.imgYt.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ytLink))
            startActivity(intent)
        }
    }

    private var oppskriftTilLagring:Meal? = null
    private fun observerOppskriftDetaljerLiveData() {
        oppskriftMvvm.observeOppskriftDetaljerLiveData().observe(this, object : Observer<Meal>{
            override fun onChanged(t: Meal?) {
                onResponseCase()
                val oppskrift = t
                oppskriftTilLagring = oppskrift
                //Hvilken kategori tilhører retten?
                binding.tvKategori.text = "Kategori: ${oppskrift!!.strCategory}"

                //Hvordan type rett det er, eksempel: indisk, amerikansk, britisk osv.
                binding.tvSted.text = "${oppskrift.strArea}"

                //Instruksjoner
                binding.tvInstruksjonerText.text = oppskrift.strInstructions

                //Ingrediensene
                binding.tvIngredienser.text = "${oppskrift.strIngredient1}, \n${oppskrift.strIngredient2}, \n${oppskrift.strIngredient3}, \n${oppskrift.strIngredient4}, \n${oppskrift.strIngredient5}, \n${oppskrift.strIngredient6}, \n${oppskrift.strIngredient7}, \n${oppskrift.strIngredient8}, \n${oppskrift.strIngredient9}, \n${oppskrift.strIngredient10}, \n${oppskrift.strIngredient11}, \n${oppskrift.strIngredient12}, \n${oppskrift.strIngredient13}, \n${oppskrift.strIngredient14}, \n${oppskrift.strIngredient15}, \n${oppskrift.strIngredient16}, \n${oppskrift.strIngredient17}, \n${oppskrift.strIngredient18}, \n${oppskrift.strIngredient19}, \n${oppskrift.strIngredient20}"

                // Youtube link
                ytLink = oppskrift.strYoutube.toString()
            }
        })
    }

    private fun setInfoIViews() {
        Glide.with(applicationContext)
            .load(oppskriftBilde)
            .into(binding.imgOppskriftDetaljer)

        binding.collapsingToolbar.title = oppskriftNavn
        binding.collapsingToolbar.setCollapsedTitleTextColor((resources.getColor(R.color.white)))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun hentOppskriftInformasjonFraIntent() {
            val intent = intent
            this.oppskriftId = intent.getStringExtra(OPPSKRIFT_ID)!!
            this.oppskriftNavn = intent.getStringExtra(OPPSKRIFT_NAVN)!!
            this.oppskriftBilde = intent.getStringExtra(OPPSKRIFT_BILDE)!!
        }

    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnFavoritt.visibility = View.INVISIBLE
        binding.tvInstruksjoner.visibility = View.INVISIBLE
        binding.tvKategori.visibility = View.INVISIBLE
        binding.tvSted.visibility = View.INVISIBLE
        binding.imgYt.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnFavoritt.visibility = View.VISIBLE
        binding.tvInstruksjoner.visibility = View.VISIBLE
        binding.tvKategori.visibility = View.VISIBLE
        binding.tvSted.visibility = View.VISIBLE
        binding.imgYt.visibility = View.VISIBLE
    }
}