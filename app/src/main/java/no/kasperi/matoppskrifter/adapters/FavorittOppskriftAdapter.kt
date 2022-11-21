package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.databinding.FavOppskriftKortBinding
import no.kasperi.matoppskrifter.pojo.MealDB

class FavorittOppskriftAdapter :
    RecyclerView.Adapter<FavorittOppskriftAdapter.FavorittViewHolder>() {
    private var favorittOppskrifter: List<MealDB> = ArrayList()
    private lateinit var onFavorittClickListener: OnFavorittClickListener
    private lateinit var onFavorittLongClickListener: OnFavorittLongClickListener

    fun setFavorittOppskriftListe(favorittOppskrifter: List<MealDB>) {
        this.favorittOppskrifter = favorittOppskrifter
        notifyDataSetChanged()
    }

    fun hentOppskriftEtterPosisjon(position: Int):MealDB{
        return favorittOppskrifter[position]
    }


    fun setOnFavorittOppskriftClickListener(onFavorittClickListener: OnFavorittClickListener) {
        this.onFavorittClickListener = onFavorittClickListener
    }

    fun setOnFavorittLongClickListener(onFavorittLongClickListener: OnFavorittLongClickListener) {
        this.onFavorittLongClickListener = onFavorittLongClickListener
    }

    class FavorittViewHolder(val binding: FavOppskriftKortBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavorittViewHolder {
        return FavorittViewHolder(FavOppskriftKortBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FavorittViewHolder, position: Int) {
        val i = position
        holder.binding.apply {
            tvOppskriftNavn.text = favorittOppskrifter[position].mealName
            Glide.with(holder.itemView)
                .load(favorittOppskrifter[position].mealThumb)
                .error(R.drawable.matoppskrifter_logo)
                .into(imgOppskrift)
        }

        holder.itemView.setOnClickListener {
            onFavorittClickListener.onFavorittClick(favorittOppskrifter[position])
        }

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                onFavorittLongClickListener.onFavorittLongCLick(favorittOppskrifter[i])
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return favorittOppskrifter.size
    }

    interface OnFavorittClickListener {
        fun onFavorittClick(meal: MealDB)
    }

    interface OnFavorittLongClickListener {
        fun onFavorittLongCLick(meal: MealDB)
    }
}