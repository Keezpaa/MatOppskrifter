package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.databinding.OppskriftKortBinding
import no.kasperi.matoppskrifter.pojo.Meal

class OppskriftAdapter : RecyclerView.Adapter<OppskriftAdapter.OppskriftViewHolder>() {

    private var oppskriftListe: List<Meal> = ArrayList()
    private lateinit var setOnOppskriftClickListener: SetOnOppskriftClickListener

    fun setKategoriListe(oppskriftListe: List<Meal>) {
        this.oppskriftListe = oppskriftListe
        notifyDataSetChanged()
    }

    fun setOnOppskriftClickListener(setOnOppskriftClickListener: SetOnOppskriftClickListener) {
        this.setOnOppskriftClickListener = setOnOppskriftClickListener
    }

    class OppskriftViewHolder(val binding: OppskriftKortBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OppskriftViewHolder {
        return OppskriftViewHolder(OppskriftKortBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: OppskriftViewHolder, position: Int) {
        holder.binding.apply {
            tvOppskriftNavn.text = oppskriftListe[position].strMeal
            Glide.with(holder.itemView)
                .load(oppskriftListe[position].strMealThumb)
                .into(imgOppskrift)
        }

        holder.itemView.setOnClickListener {
            setOnOppskriftClickListener.setOnClickListener(oppskriftListe[position])
        }
    }

    override fun getItemCount(): Int {
        return oppskriftListe.size
    }
}

interface SetOnOppskriftClickListener {
    fun setOnClickListener(meal: Meal)
}
