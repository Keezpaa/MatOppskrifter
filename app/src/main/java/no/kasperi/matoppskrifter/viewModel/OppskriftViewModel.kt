package no.kasperi.matoppskrifter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.OppskriftRespons
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OppskriftViewModel():ViewModel() {
    private var mutableOppskrift= MutableLiveData<List<Meal>>()

    fun hentOppskriftEtterKategori(kategori:String){
        RetrofitInstance.api.hentOppskriftEtterKategori(kategori).enqueue(object : Callback<OppskriftRespons>{
            override fun onResponse(call: Call<OppskriftRespons>, response: Response<OppskriftRespons>) {
                mutableOppskrift.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<OppskriftRespons>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }
        })
    }

    fun observeMeal():LiveData<List<Meal>>{
        return mutableOppskrift
    }
}