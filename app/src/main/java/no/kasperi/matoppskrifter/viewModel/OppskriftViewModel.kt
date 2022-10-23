package no.kasperi.matoppskrifter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.OppskriftListe
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OppskriftViewModel(): ViewModel() {
    private var oppskriftDetaljerLiveData = MutableLiveData<Meal>()

    fun hentOppskriftDetaljer(id:String){
        RetrofitInstance.api.hentOppskriftDetaljer(id).enqueue(object : Callback<OppskriftListe>{
            override fun onResponse(
                call: Call<OppskriftListe>,
                response: Response<OppskriftListe>
            ) {
                if(response.body()!=null){
                    oppskriftDetaljerLiveData.value = response.body()!!.meals[0]
                }
                else
                    return

            }

            override fun onFailure(call: Call<OppskriftListe>, t: Throwable) {
                Log.d("OppskriftActivity", t.message.toString())
            }
        })
    }

    fun observeOppskriftDetaljerLiveData():LiveData<Meal>{
        return oppskriftDetaljerLiveData
    }

}