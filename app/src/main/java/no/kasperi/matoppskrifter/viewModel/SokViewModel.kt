package no.kasperi.matoppskrifter.viewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kasperi.matoppskrifter.pojo.*
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SokViewModel : ViewModel()  {
    private val soktOppskriftLiveData = MutableLiveData<Meal>()


    fun sokOppskriftDetaljer(name: String,context: Context?) {
        RetrofitInstance.api.sokEtterOppskrift(name).enqueue(object : Callback<OppskriftListe> {
            override fun onResponse(call: Call<OppskriftListe>, response: Response<OppskriftListe>) {
                if (response.body()?.meals == null)
                    Toast.makeText(context?.applicationContext, "Ingen oppskrift med det navnet", Toast.LENGTH_SHORT).show()
                else
                    soktOppskriftLiveData.value = response.body()!!.meals[0]
            }

            override fun onFailure(call: Call<OppskriftListe>, t: Throwable) {
                Log.e("HjemViewModel", t.message.toString())
            }

        })
    }

    fun observerSoktLiveData(): LiveData<Meal> {
        return soktOppskriftLiveData
    }
}