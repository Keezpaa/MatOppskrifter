package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.databinding.EnkelOppskriftKortBinding
import no.kasperi.matoppskrifter.pojo.Meal

class EnkelOppskriftsKortRecyclerAdapter(): RecyclerView.Adapter<EnkelOppskriftsKortRecyclerAdapter.EnkelOppskriftViewHolder>() {
    private var oppskriftListe: List<Meal> = ArrayList()
    fun setOppskriftListe(oppskriftListe: List<Meal>) {
        this.oppskriftListe = oppskriftListe
    }

    class EnkelOppskriftViewHolder(val binding: EnkelOppskriftKortBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnkelOppskriftViewHolder {
        return EnkelOppskriftViewHolder(EnkelOppskriftKortBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: EnkelOppskriftViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(holder.itemView)
                .load(oppskriftListe[position].strMealThumb)
                .into(imgOppskrift)
        }
    }

    override fun getItemCount(): Int {
        return oppskriftListe.size
    }
}