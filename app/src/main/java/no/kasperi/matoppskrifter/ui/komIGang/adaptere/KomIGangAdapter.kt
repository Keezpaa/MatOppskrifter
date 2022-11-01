package no.kasperi.matoppskrifter.ui.komIGang.adaptere

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.abstraction.DiffUtilClass
import no.kasperi.matoppskrifter.modeller.LokalModell

class KomIGangAdapter : ListAdapter<LokalModell, KomIGangViewHolder>(DiffUtilClass<LokalModell>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KomIGangViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_komigang_element, parent, false)
        return KomIGangViewHolder(view)
    }

    override fun onBindViewHolder(holder: KomIGangViewHolder, position: Int) =
        holder.present(getItem(position))
}