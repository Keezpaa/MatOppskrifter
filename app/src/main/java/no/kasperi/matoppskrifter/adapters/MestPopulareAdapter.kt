package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.databinding.PopulareRetterBinding
import no.kasperi.matoppskrifter.pojo.Meal

class MestPopulareAdapter : RecyclerView.Adapter<MestPopulareAdapter.MestPopularOppskriftViewHolder>(){
    private var oppskriftListe: List<Meal> = ArrayList()
    private lateinit var onItemClick: OnItemClick
    private lateinit var onLongItemClick:OnLongItemClick
    fun setOppskriftListe(oppskriftListe: List<Meal>) {
        this.oppskriftListe = oppskriftListe
        notifyDataSetChanged()
    }

    fun setOnClickListener(onItemClick: OnItemClick){
        this.onItemClick = onItemClick
    }

    fun setOnLongCLickListener(onLongItemClick:OnLongItemClick){
        this.onLongItemClick = onLongItemClick
    }

    class MestPopularOppskriftViewHolder(val binding:PopulareRetterBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MestPopularOppskriftViewHolder {
        return MestPopularOppskriftViewHolder(PopulareRetterBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MestPopularOppskriftViewHolder, position: Int) {
        val i = position

            Glide.with(holder.itemView)
                .load(oppskriftListe[position].strMealThumb)
                .into(holder.binding.imgPopularOppskrift)


        holder.itemView.setOnClickListener {
            onItemClick.onItemClick(oppskriftListe[position])
        }

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                onLongItemClick.onItemLongClick(oppskriftListe[i])
                return true
            }

        })
    }

    override fun getItemCount(): Int {
        return oppskriftListe.size
    }
}

interface OnItemClick{
    fun onItemClick(meal:Meal)
}

interface OnLongItemClick{
    fun onItemLongClick(meal:Meal)
}