package no.kasperi.matoppskrifter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kasperi.matoppskrifter.pojo.OppskriftFraKategori
import no.kasperi.matoppskrifter.pojo.OppskriftFraKategoriListe
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KategoriViewModel : ViewModel() {

    val oppskrifterLiveData = MutableLiveData<List<OppskriftFraKategori>>()

    fun hentOppskriftFraKategori(kategoriNavn:String){
        RetrofitInstance.api.hentOppskrftFraKategori(kategoriNavn).enqueue(object : Callback<OppskriftFraKategoriListe>{
            override fun onResponse(
                call: Call<OppskriftFraKategoriListe>,
                response: Response<OppskriftFraKategoriListe>
            ) {
                response.body()?.let { oppskriftListe ->
                    oppskrifterLiveData.postValue(oppskriftListe.meals)
                }
            }

            override fun onFailure(call: Call<OppskriftFraKategoriListe>, t: Throwable) {
                Log.e("KategoriViewModel", t.message.toString())
            }
        })
    }

    fun observeOppskrifterLiveData():LiveData<List<OppskriftFraKategori>>{
        return oppskrifterLiveData
    }
}