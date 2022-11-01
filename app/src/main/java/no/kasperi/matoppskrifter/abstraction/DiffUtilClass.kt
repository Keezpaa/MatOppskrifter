package no.kasperi.matoppskrifter.abstraction

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import no.kasperi.matoppskrifter.modeller.LokalModell

class DiffUtilClass<T : LokalModell> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}