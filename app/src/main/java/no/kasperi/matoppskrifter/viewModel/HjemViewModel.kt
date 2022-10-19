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

class HjemViewModel():ViewModel() {

    private var randomOppskriftLiveData = MutableLiveData<Meal>()

    fun getRandomOppskrift(){
        RetrofitInstance.api.getRandomOppskrift().enqueue(object : Callback<OppskriftListe> {
            override fun onResponse(
                call: Call<OppskriftListe>,
                response: Response<OppskriftListe>
            ) {
                if(response.body() != null) {
                    val randomOppskrift: Meal = response.body()!!.meals[0]
                    randomOppskriftLiveData.value = randomOppskrift
                }else {
                    return
                }
            }

            override fun onFailure(call: Call<OppskriftListe>, t: Throwable) {
                Log.d("HjemFragment", t.message.toString())
            }
        })
    }

    fun observeRandomOppskriftLiveData():LiveData<Meal>{
        return randomOppskriftLiveData
    }
}