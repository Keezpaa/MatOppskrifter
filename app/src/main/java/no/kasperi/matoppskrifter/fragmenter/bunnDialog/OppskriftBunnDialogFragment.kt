package no.kasperi.matoppskrifter.fragmenter.bunnDialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.aktiviteter.MainActivity
import no.kasperi.matoppskrifter.databinding.FragmentOppskriftBunnDialogBinding
import no.kasperi.matoppskrifter.viewModel.HjemViewModel


private const val OPPSKRIFT_ID = "param1"


class OppskriftBunnDialogFragment : BottomSheetDialogFragment() {
    private var oppskriftId: String? = null
    private lateinit var binding:FragmentOppskriftBunnDialogBinding
    private lateinit var viewModel:HjemViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            oppskriftId = it.getString(OPPSKRIFT_ID)
        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOppskriftBunnDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oppskriftId?.let { viewModel.hentOppskriftMedId(it) }

        observerBunnDialog()
    }

    private fun observerBunnDialog() {
        viewModel.observerBunnDialogOppskrift().observe(viewLifecycleOwner, Observer { oppskrift ->
            Glide.with(this).load(oppskrift.strMealThumb).into(binding.imgBunnDialog)
            binding.bunnDialogSted.text = oppskrift.strArea
            binding.bunnDialogKategori.text = oppskrift.strCategory
            binding.tvBunnKategoriNavn.text = oppskrift.strMeal
        })
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            OppskriftBunnDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(OPPSKRIFT_ID, param1)
                }
            }
    }
}