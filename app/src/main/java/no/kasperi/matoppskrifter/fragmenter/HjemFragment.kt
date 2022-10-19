package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.databinding.FragmentHjemBinding
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.OppskriftListe
import no.kasperi.matoppskrifter.viewModel.HjemViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HjemFragment : Fragment() {

    private lateinit var binding:FragmentHjemBinding
    private lateinit var hjemMvvm:HjemViewModel
    private lateinit var randomOppskrift:Meal

    companion object {
        const val OPPSKRIFT_ID = "no.kasperi.matoppskrifter.fragmenter.idMeal"
        const val OPPSKRIFT_NAME = "no.kasperi.matoppskrifter.fragmenter.nameMeal"
        const val OPPSKRIFT_THUMB = "no.kasperi.matoppskrifter.fragmenter.thumbMeal"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hjemMvvm = ViewModelProvider(this)[HjemViewModel::class.java]
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHjemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hjemMvvm.getRandomOppskrift()
        observeRandomOppskrift()
        onRandomOppskriftClick()
    }

    private fun onRandomOppskriftClick() {
        binding.randomOppskrift.setOnClickListener {
            val intent = Intent(activity, OppskriftActivity::class.java)
            intent.putExtra(OPPSKRIFT_ID, randomOppskrift.idMeal)
            intent.putExtra(OPPSKRIFT_NAME, randomOppskrift.strMeal)
            intent.putExtra(OPPSKRIFT_THUMB, randomOppskrift.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomOppskrift() {
            hjemMvvm.observeRandomOppskriftLiveData().observe(viewLifecycleOwner)
             { meal ->
                Glide.with(this@HjemFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomOppskrift)

                this.randomOppskrift = meal
            }
    }
}