package no.kasperi.matoppskrifter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.kasperi.matoppskrifter.pojo.*
import no.kasperi.matoppskrifter.db.OppskriftDB
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

const val TAG = "HjemViewModel"

class HjemViewModel : ViewModel() {

    private val mutableCategory = MutableLiveData<KategoriListe>()
    private val mutableRandomMeal = MutableLiveData<RandomMealResponse>()
    private val mutableMealsByCategory = MutableLiveData<MealsResponse>()


    init {
        getRandomMeal()
        getAllCategories()
        getMealsByCategory("beef")
    }


    private fun getAllCategories() {
        RetrofitInstance.api.hentKategorier().enqueue(object : Callback<KategoriListe> {
            override fun onResponse(call: Call<KategoriListe>, response: Response<KategoriListe>) {
                mutableCategory.value = response.body()
            }

            override fun onFailure(call: Call<KategoriListe>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    private fun getRandomMeal() {
        RetrofitInstance.api.hentTilfeldigOppskrift().enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(call: Call<RandomMealResponse>, response: Response<RandomMealResponse>) {
                mutableRandomMeal.value = response.body()
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    private fun getMealsByCategory(kategori:String) {

        RetrofitInstance.api.hentOppskriftFraKategori(kategori).enqueue(object : Callback<MealsResponse> {
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                mutableMealsByCategory.value = response.body()
            }

            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun observeMealByCategory(): LiveData<MealsResponse> {
        return mutableMealsByCategory
    }

    fun observeRandomMeal(): LiveData<RandomMealResponse> {
        return mutableRandomMeal
    }

    fun observeCategories(): LiveData<KategoriListe> {
        return mutableCategory
    }

}