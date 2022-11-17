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
    private val soktEtterOppskriftLiveData = MutableLiveData<MealDetail>()


    fun sokOppskriftDetaljer(name: String,context: Context?) {
        RetrofitInstance.api.sokEtterOppskrift(name).enqueue(object : Callback<TilfeldigOppskriftRespons> {
            override fun onResponse(call: Call<TilfeldigOppskriftRespons>, response: Response<TilfeldigOppskriftRespons>) {
                if (response.body()?.meals == null)
                    Toast.makeText(context?.applicationContext, "Ingen oppskrift med det navnet", Toast.LENGTH_SHORT).show()
                else
                    soktEtterOppskriftLiveData.value = response.body()!!.meals[0]
            }

            override fun onFailure(call: Call<TilfeldigOppskriftRespons>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun observerSoktLiveData(): LiveData<MealDetail> {
        return soktEtterOppskriftLiveData
    }
}