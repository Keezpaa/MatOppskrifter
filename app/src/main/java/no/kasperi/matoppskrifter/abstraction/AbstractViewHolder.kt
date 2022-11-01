package no.kasperi.matoppskrifter.abstraction

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import no.kasperi.matoppskrifter.modeller.LokalModell

abstract class AbstractViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {



    fun bindData(data: LokalModell) {
        itemView.tag = data
        presentData(data)
    }

    abstract fun presentData(data: LokalModell)
}