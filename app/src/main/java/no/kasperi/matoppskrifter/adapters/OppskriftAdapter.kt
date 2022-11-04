package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.databinding.OppskriftElementBinding
import no.kasperi.matoppskrifter.pojo.Meal

class OppskriftAdapter : RecyclerView.Adapter<OppskriftAdapter.FavorittAdapterViewHolder>() {
    var onItemClick:((Meal) -> Unit)?= null
    private lateinit var onFavorittClickListener: OnFavorittClickListener
    private lateinit var setOnMealClickListener: SetOnMealClickListener

    inner class FavorittAdapterViewHolder(val binding:OppskriftElementBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavorittAdapterViewHolder {
        return FavorittAdapterViewHolder(
            OppskriftElementBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavorittAdapterViewHolder, position: Int) {
        val oppskrift = differ.currentList[position]
        Glide.with(holder.itemView).load(oppskrift.strMealThumb).into(holder.binding.imgOppskrift)
        holder.binding.tvOppskriftNavn.text = oppskrift.strMeal
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }




    interface OnFavorittClickListener {
        fun onFavorittClick(oppskrift: Meal)
    }

    fun setOnFavorittClickListener(onFavorittClickListener: OnFavorittClickListener) {
        this.onFavorittClickListener = onFavorittClickListener
    }

    interface SetOnMealClickListener {
        fun setOnClickListener(meal: Meal)
    }
}