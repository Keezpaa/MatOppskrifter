package no.kasperi.matoppskrifter.fragmenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.adapters.OppskriftAdapter
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.databinding.FragmentSokBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.OppskriftListe
import no.kasperi.matoppskrifter.viewModel.SokViewModel


class SokFragment : Fragment() {
    private lateinit var binding:FragmentSokBinding
    private lateinit var sokViewModel: SokViewModel
    private lateinit var oppskriftAdapter: OppskriftAdapter
    private var oppskriftId = "no.kasperi.matoppskrifter.fragmenter.idOppskrift"
    private var oppskriftNavn = "no.kasperi.matoppskrifter.fragmenter.navnOppskrift"
    private var oppskriftBilde = "no.kasperi.matoppskrifter.fragmenter.bildeOppskrift"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oppskriftAdapter = OppskriftAdapter()
        sokViewModel = ViewModelProvider(this)[SokViewModel::class.java]
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSokBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSokClick()
        observerSokLiveData()
        setOnOppskriftKortClick()

    }

    private fun setOnOppskriftKortClick() {
        binding.oppskriftKort.setOnClickListener {
            val intent = Intent(context, OppskriftActivity::class.java)

            intent.putExtra(OPPSKRIFT_ID, oppskriftId)
            intent.putExtra(OPPSKRIFT_NAVN, oppskriftNavn)
            intent.putExtra(OPPSKRIFT_BILDE, oppskriftBilde)

            startActivity(intent)


        }
    }


    private fun onSokClick() {
        binding.ikonSokeknapp.setOnClickListener {
            sokViewModel.sokOppskriftDetaljer(binding.etSokefelt.text.toString(),context)

        }
    }

    private fun observerSokLiveData() {
        sokViewModel.observerSoktLiveData()
            .observe(viewLifecycleOwner, object : Observer<Meal> {
                override fun onChanged(t: Meal?) {
                    if (t == null) {
                        Toast.makeText(context, "Ingen oppskrift med det navnet", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.apply {

                            oppskriftId = t.idMeal
                            oppskriftNavn = t.strMeal.toString()
                            oppskriftBilde = t.strMealThumb.toString()

                            Glide.with(context!!.applicationContext)
                                .load(t.strMealThumb)
                                .into(bildeOppskriftKort)

                            tvOppskriftKort.text = t.strMeal
                            oppskriftKort.visibility = View.VISIBLE
                        }
                    }
                }
            })
    }


}