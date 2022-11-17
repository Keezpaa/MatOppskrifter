package no.kasperi.matoppskrifter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kasperi.matoppskrifter.pojo.*
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "HjemViewModel"

class HjemViewModel:ViewModel() {

    private val mutableCategory = MutableLiveData<KategoriRespons>()
    private val mutableRandomMeal = MutableLiveData<TilfeldigOppskriftRespons>()
    private val mutableMealsByCategory = MutableLiveData<OppskriftRespons>()


    init {
        hentTilfeldigOppskrift()
        hentAlleKategorier()
        hentOppskriftEtterKategori()
    }


    private fun hentAlleKategorier() {
        RetrofitInstance.api.hentKategorier().enqueue(object : Callback<KategoriRespons> {
            override fun onResponse(call: Call<KategoriRespons>, response: Response<KategoriRespons>) {
                mutableCategory.value = response.body()
            }

            override fun onFailure(call: Call<KategoriRespons>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    private fun hentTilfeldigOppskrift() {
        RetrofitInstance.api.hentTilfeldigOppskrift().enqueue(object : Callback<TilfeldigOppskriftRespons> {
            override fun onResponse(call: Call<TilfeldigOppskriftRespons>, response: Response<TilfeldigOppskriftRespons>) {
                mutableRandomMeal.value = response.body()
            }

            override fun onFailure(call: Call<TilfeldigOppskriftRespons>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }



    private fun hentOppskriftEtterKategori() {
        RetrofitInstance.api.hentOppskriftEtterKategori("beef").enqueue(object : Callback<OppskriftRespons> {
            override fun onResponse(call: Call<OppskriftRespons>, response: Response<OppskriftRespons>) {
                mutableMealsByCategory.value = response.body()
            }

            override fun onFailure(call: Call<OppskriftRespons>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun observeMealByCategory(): LiveData<OppskriftRespons> {
        return mutableMealsByCategory
    }

    fun observeRandomMeal(): LiveData<TilfeldigOppskriftRespons> {
        return mutableRandomMeal
    }

    fun observeCategories(): LiveData<KategoriRespons> {
        return mutableCategory
    }

}