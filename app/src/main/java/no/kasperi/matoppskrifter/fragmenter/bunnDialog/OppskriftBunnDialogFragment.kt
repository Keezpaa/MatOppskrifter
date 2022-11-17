package no.kasperi.matoppskrifter.fragmenter.bunnDialog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.aktiviteter.MainActivity
import no.kasperi.matoppskrifter.aktiviteter.OppskriftActivity
import no.kasperi.matoppskrifter.aktiviteter.OppskriftDetaljerActivity
import no.kasperi.matoppskrifter.databinding.FragmentOppskriftBunnDialogBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.CATEGORY_NAME
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.MEAL_AREA
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.viewModel.HjemViewModel


class OppskriftBunnDialogFragment  : BottomSheetDialogFragment() {
    private var matNavn = "no.kasperi.matoppskrifter.mealName"
    private var matId ="no.kasperi.matoppskrifter.mealId"
    private var matBilde = "no.kasperi.matoppskrifter.thumbMeal"
    private var matSted = "no.kasperi.matoppskrifter.mealCountry"
    private var matKategori = "no.kasperi.matoppskrifter.mealCategory"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = arguments
        matNavn = b!!.getString(OPPSKRIFT_NAVN).toString()
        matId =b!!.getString(OPPSKRIFT_ID).toString()
        matBilde =b!!.getString(OPPSKRIFT_BILDE).toString()
        matKategori =b!!.getString(CATEGORY_NAME).toString()
        matSted =b!!.getString(MEAL_AREA).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = LayoutInflater.from(context).inflate(R.layout.fragment_oppskrift_bunn_dialog, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareView(view)

        view.setOnClickListener {
            val intent = Intent(context, OppskriftDetaljerActivity::class.java)
            intent.putExtra(OPPSKRIFT_ID,matId)
            intent.putExtra(OPPSKRIFT_NAVN,matNavn)
            intent.putExtra(OPPSKRIFT_BILDE,matBilde)
            startActivity(intent)
        }

    }

    fun prepareView(view:View){
        val tvMealName = view.findViewById<TextView>(R.id.tv_bunn_kategori_navn)
        val tvMealCategory = view.findViewById<TextView>(R.id.bunn_dialog_kategori)
        val tvMealCountry = view.findViewById<TextView>(R.id.bunn_dialog_sted)
        val imgMeal = view.findViewById<ImageView>(R.id.img_bunn_dialog)

        Glide.with(view)
            .load(matBilde)
            .into(imgMeal)
        tvMealName.text = matNavn
        tvMealCategory.text = matKategori
        tvMealCountry.text = matSted
    }


}