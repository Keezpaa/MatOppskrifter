package no.kasperi.matoppskrifter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kasperi.matoppskrifter.pojo.KategoriListe
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.OppskriftKategori
import no.kasperi.matoppskrifter.pojo.OppskriftListe
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HjemViewModel():ViewModel() {

    private var randomOppskriftLiveData = MutableLiveData<Meal>()
    private var populareRetterLiveData = MutableLiveData<List<OppskriftKategori>>()

    fun hentTilfeldigOppskrift(){
        RetrofitInstance.api.hentTilfeldigOppskrift().enqueue(object : Callback<OppskriftListe> {
            override fun onResponse(
                call: Call<OppskriftListe>,
                response: Response<OppskriftListe>
            ) {
                if(response.body() != null) {
                    val tilfeldigOppskrift: Meal = response.body()!!.meals[0]
                    randomOppskriftLiveData.value = tilfeldigOppskrift
                }else {
                    return
                }
            }

            override fun onFailure(call: Call<OppskriftListe>, t: Throwable) {
                Log.d("HjemFragment", t.message.toString())
            }
        })
    }

    fun hentPopulareRetter() {
       RetrofitInstance.api.hentPopulareRetter("Beef").enqueue(object : Callback<KategoriListe>{
           override fun onResponse(call: Call<KategoriListe>, response: Response<KategoriListe>) {
               if(response.body() != null){
                   populareRetterLiveData.value = response.body()!!.meals
               }
           }

           override fun onFailure(call: Call<KategoriListe>, t: Throwable) {
               Log.d("HjemFragment", t.message.toString())
           }
       })
    }

    fun observeTilfeldigOppskriftLiveData():LiveData<Meal>{
        return randomOppskriftLiveData
    }

    fun observePopulareRetterLiveData():LiveData<List<OppskriftKategori>>{
        return populareRetterLiveData
    }
}