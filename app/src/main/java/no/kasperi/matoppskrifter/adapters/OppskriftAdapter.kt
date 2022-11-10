package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.databinding.OppskriftKortBinding
import no.kasperi.matoppskrifter.pojo.Meal

class OppskriftAdapter : RecyclerView.Adapter<OppskriftAdapter.MealViewHolder>() {

    private var mealList: List<Meal> = ArrayList()
    private lateinit var setOnMealClickListener: SetOnMealClickListener

    fun setCategoryList(mealList: List<Meal>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    fun setOnMealClickListener(setOnMealClickListener: SetOnMealClickListener) {
        this.setOnMealClickListener = setOnMealClickListener
    }

    class MealViewHolder(val binding: OppskriftKortBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(OppskriftKortBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.binding.apply {
            tvOppskriftNavn.text = mealList[position].strMeal
            Glide.with(holder.itemView)
                .load(mealList[position].strMealThumb)
                .into(imgOppskrift)
        }

        holder.itemView.setOnClickListener {
            setOnMealClickListener.setOnClickListener(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}

interface SetOnMealClickListener {
    fun setOnClickListener(meal: Meal)
}
