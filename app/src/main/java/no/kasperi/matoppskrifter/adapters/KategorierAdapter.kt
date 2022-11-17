package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.databinding.KategoriElementBinding
import no.kasperi.matoppskrifter.pojo.Kategori

class KategorierAdapter():RecyclerView.Adapter<KategorierAdapter.KategoriViewHolder>() {

    private var kategoriListe: List<Kategori> = ArrayList()
    private lateinit var onItemClick: OnItemCategoryClicked

    fun setKategoriListe(kategoriListe:List<Kategori>){
        this.kategoriListe = kategoriListe
        notifyDataSetChanged()
    }

    fun onItemClicked(onItemClick: OnItemCategoryClicked){
        this.onItemClick = onItemClick
    }
    class KategoriViewHolder(val binding:KategoriElementBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        return KategoriViewHolder(
            KategoriElementBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        holder.binding.apply {
            tvKategoriNavn.text = kategoriListe[position].strCategory

            Glide.with(holder.itemView)
                .load(kategoriListe[position].strCategoryThumb)
                .into(imgKategori)
        }

        holder.itemView.setOnClickListener {
            onItemClick.onClickListener(kategoriListe[position])
        }
    }

    override fun getItemCount(): Int {
        return kategoriListe.size
    }
    interface OnItemCategoryClicked{
        fun onClickListener(kategori:Kategori)
    }
}