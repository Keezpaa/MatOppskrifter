package no.kasperi.matoppskrifter.ui.komIGang.adaptere

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.holder_komigang_element.view.*
import no.kasperi.matoppskrifter.modeller.KomIGangElement
import no.kasperi.matoppskrifter.modeller.LokalModell

class KomIGangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun present(item: LokalModell) {
        when (item) {
            is KomIGangElement -> {
                itemView.komigang_bilde.setImageResource(item.komigangBilde)
                itemView.komigang_tittel.text = item.tittel
                itemView.komigang_beskrivelse.text = item.beskrivelse
            }
        }
    }
}