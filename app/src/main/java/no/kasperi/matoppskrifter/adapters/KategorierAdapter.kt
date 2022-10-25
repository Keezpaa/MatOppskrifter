package no.kasperi.matoppskrifter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kasperi.matoppskrifter.databinding.KategoriElementBinding
import no.kasperi.matoppskrifter.pojo.Kategori

class KategorierAdapter():RecyclerView.Adapter<KategorierAdapter.KategoriViewHolder>() {

    private var kategoriListe = ArrayList<Kategori>()
    var onItemClick : ((Kategori) -> Unit)? = null

    fun setKategoriListe(kategoriListe:List<Kategori>){
        this.kategoriListe = kategoriListe as ArrayList<Kategori>
        notifyDataSetChanged()
    }

    inner class KategoriViewHolder(val binding:KategoriElementBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        return KategoriViewHolder(
            KategoriElementBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        Glide.with(holder.itemView).load(kategoriListe[position].strCategoryThumb).into(holder.binding.imgKategori)
        holder.binding.tvKategoriNavn.text = kategoriListe[position].strCategory

        holder.itemView.setOnClickListener{
            onItemClick!!.invoke(kategoriListe[position])
        }
    }

    override fun getItemCount(): Int {
        return kategoriListe.size
    }
}