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
    private var favoriteMeals: List<MealDB> = ArrayList()
    private lateinit var onFavoriteClickListener: OnFavoriteClickListener
    private lateinit var onFavoriteLongClickListener: OnFavoriteLongClickListener

    fun setFavoriteMealsList(favoriteMeals: List<MealDB>) {
        this.favoriteMeals = favoriteMeals
        notifyDataSetChanged()
    }

    fun getMelaByPosition(position: Int):MealDB{
        return favoriteMeals[position]
    }


    fun setOnFavoriteMealClickListener(onFavoriteClickListener: OnFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener
    }

    fun setOnFavoriteLongClickListener(onFavoriteLongClickListener: OnFavoriteLongClickListener) {
        this.onFavoriteLongClickListener = onFavoriteLongClickListener
    }

    class FavorittViewHolder(val binding: FavOppskriftKortBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavorittViewHolder {
        return FavorittViewHolder(FavOppskriftKortBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FavorittViewHolder, position: Int) {
        val i = position
        holder.binding.apply {
            tvOppskriftNavn.text = favoriteMeals[position].mealName
            Glide.with(holder.itemView)
                .load(favoriteMeals[position].mealThumb)
                .error(R.drawable.matoppskrifter_logo)
                .into(imgOppskrift)
        }

        holder.itemView.setOnClickListener {
            onFavoriteClickListener.onFavoriteClick(favoriteMeals[position])
        }

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                onFavoriteLongClickListener.onFavoriteLongCLick(favoriteMeals[i])
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return favoriteMeals.size
    }

    interface OnFavoriteClickListener {
        fun onFavoriteClick(meal: MealDB)
    }

    interface OnFavoriteLongClickListener {
        fun onFavoriteLongCLick(meal: MealDB)
    }
}