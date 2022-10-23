package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.databinding.PopulareRetterBinding
import no.kasperi.matoppskrifter.pojo.KategoriListe
import no.kasperi.matoppskrifter.pojo.OppskriftKategori

class MestPopulareAdapter(): RecyclerView.Adapter<MestPopulareAdapter.PopularOppskriftViewHolder>() {
    lateinit var onItemClick:((OppskriftKategori) -> Unit)
    private var oppskriftListe = ArrayList<OppskriftKategori>()

    fun setOppskrifter(oppskriftListe:ArrayList<OppskriftKategori>){
        this.oppskriftListe = oppskriftListe
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularOppskriftViewHolder {
        return PopularOppskriftViewHolder(PopulareRetterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularOppskriftViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(oppskriftListe[position].strMealThumb)
            .into(holder.binding.bildePopOppskriftElement)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(oppskriftListe[position])
        }
    }

    override fun getItemCount(): Int {
        return oppskriftListe.size
    }

    class PopularOppskriftViewHolder(var binding:PopulareRetterBinding):RecyclerView.ViewHolder(binding.root)
}