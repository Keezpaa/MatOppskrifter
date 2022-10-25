package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.databinding.OppskriftElementBinding
import no.kasperi.matoppskrifter.pojo.OppskriftFraKategori
import no.kasperi.matoppskrifter.pojo.OppskriftListe
import no.kasperi.matoppskrifter.viewModel.KategoriViewModel

class OppskriftKategoriAdapter : RecyclerView.Adapter<OppskriftKategoriAdapter.KategoriViewModel>(){
    private var oppskriftListe = ArrayList<OppskriftFraKategori>()

    fun setOppskriftListe(oppskriftListe:List<OppskriftFraKategori>){
        this.oppskriftListe = oppskriftListe as ArrayList<OppskriftFraKategori>
        notifyDataSetChanged()
    }

    inner class KategoriViewModel(val binding:OppskriftElementBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewModel {
        return KategoriViewModel(
            OppskriftElementBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: KategoriViewModel, position: Int) {
        Glide.with(holder.itemView).load(oppskriftListe[position].strMealThumb).into(holder.binding.imgOppskrift)
        holder.binding.tvOppskriftNavn.text = oppskriftListe[position].strMeal
    }

    override fun getItemCount(): Int {
        return oppskriftListe.size
    }
}