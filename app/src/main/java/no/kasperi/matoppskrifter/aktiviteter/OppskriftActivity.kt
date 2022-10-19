package no.kasperi.matoppskrifter.aktiviteter

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.databinding.ActivityOppskriftBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAME
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_THUMB
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.viewModel.OppskriftViewModel

class OppskriftActivity : AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var binding: ActivityOppskriftBinding
    private lateinit var oppskriftMvvm:OppskriftViewModel

    private lateinit var ytLink:String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOppskriftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        oppskriftMvvm = ViewModelProvider(this)[OppskriftViewModel::class.java]

        getOppskriftInformasjonFraIntent()
        setInfoInViews()

        loadingCase()
        oppskriftMvvm.getOppskriftDetaljer(mealId)
        observerOppskriftDetaljerLiveData()

        onYoutubeClick()
    }

    private fun onYoutubeClick() {
        binding.imgYt.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ytLink))
            startActivity(intent)
        }
    }

    private fun observerOppskriftDetaljerLiveData() {
        oppskriftMvvm.observeOppskriftDetaljerLiveData().observe(this, object : Observer<Meal>{
            override fun onChanged(t: Meal?) {
                onResponseCase()
                val oppskrift = t

                binding.tvKategori.text = "Kategori: ${oppskrift!!.strCategory}"
                binding.tvSted.text = "${oppskrift.strArea}"
                binding.tvInstruksjonerText.text = oppskrift.strInstructions

                ytLink = oppskrift.strYoutube
            }
        })
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgOppskriftDetaljer)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor((resources.getColor(R.color.white)))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getOppskriftInformasjonFraIntent() {
            val intent = intent
            this.mealId = intent.getStringExtra(OPPSKRIFT_ID)!!
            this.mealName = intent.getStringExtra(OPPSKRIFT_NAME)!!
            this.mealThumb = intent.getStringExtra(OPPSKRIFT_THUMB)!!
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