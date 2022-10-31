package no.kasperi.matoppskrifter.viewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.OppskriftListe
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SokViewModel : ViewModel()  {
    private val soktOppskriftLiveData = MutableLiveData<List<Meal>>()


    fun sokEtterOppskrifter(searchQuery:String) = RetrofitInstance.api.sokEtterOppskrift(searchQuery).enqueue(
        object : Callback<OppskriftListe>{
            override fun onResponse(
                call: Call<OppskriftListe>,
                response: Response<OppskriftListe>
            ) {
                val oppskriftListe = response.body()?.meals
                oppskriftListe?.let{
                    soktOppskriftLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<OppskriftListe>, t: Throwable) {
                Log.e("HjemViewModel", t.message.toString())
            }
        }
    )

    fun observerSoktLiveData(): LiveData<List<Meal>> = soktOppskriftLiveData
}