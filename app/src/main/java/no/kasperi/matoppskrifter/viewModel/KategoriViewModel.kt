package no.kasperi.matoppskrifter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kasperi.matoppskrifter.pojo.Kategori
import no.kasperi.matoppskrifter.pojo.KategoriRespons
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* TAG = HjemViewModel */
class KategoriViewModel : ViewModel() {

    private var kategorier: MutableLiveData<List<Kategori>> = MutableLiveData<List<Kategori>>()

    init {
        hentKategorier()
    }

    private fun hentKategorier(){
        RetrofitInstance.api.hentKategorier().enqueue(object : Callback<KategoriRespons>{
            override fun onResponse(call: Call<KategoriRespons>, response: Response<KategoriRespons>) {
                /* Må stå categories på slutten av response.body() pga apiet er engelsk og krever det */
                kategorier.value = response.body()!!.categories
            }

            override fun onFailure(call: Call<KategoriRespons>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }

        })
    }

    fun observerKategorier():LiveData<List<Kategori>>{
        return kategorier
    }
}