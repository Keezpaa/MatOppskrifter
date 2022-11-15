package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.databinding.PopulareRetterBinding
import no.kasperi.matoppskrifter.pojo.Meal

class MestPopulareAdapter : RecyclerView.Adapter<MestPopulareAdapter.MostPopularMealViewHolder>(){
    private var mealsList: List<Meal> = ArrayList()
    private lateinit var onItemClick: OnItemClick
    private lateinit var onLongItemClick:OnLongItemClick
    fun setMealList(mealsList: List<Meal>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    fun setOnClickListener(onItemClick: OnItemClick){
        this.onItemClick = onItemClick
    }

    fun setOnLongCLickListener(onLongItemClick:OnLongItemClick){
        this.onLongItemClick = onLongItemClick
    }

    class MostPopularMealViewHolder(val binding:PopulareRetterBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostPopularMealViewHolder {
        return MostPopularMealViewHolder(PopulareRetterBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MostPopularMealViewHolder, position: Int) {
        val i = position

            Glide.with(holder.itemView)
                .load(mealsList[position].strMealThumb)
                .into(holder.binding.imgPopularOppskrift)


        holder.itemView.setOnClickListener {
            onItemClick.onItemClick(mealsList[position])
        }

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                onLongItemClick.onItemLongClick(mealsList[i])
                return true
            }

        })
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}

interface OnItemClick{
    fun onItemClick(meal:Meal)
}

interface OnLongItemClick{
    fun onItemLongClick(meal:Meal)
}