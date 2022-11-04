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

class HjemViewModel(
    private val oppskriftDB: OppskriftDB
):ViewModel() {

    private var randomOppskriftLiveData = MutableLiveData<Meal>()
    private var populareRetterLiveData = MutableLiveData<List<OppskriftFraKategori>>()
    private var kategorierLiveData = MutableLiveData<List<Kategori>>()
    private var favorittOppskriftLiveData = oppskriftDB.oppskriftDao().hentAlleOppskrifter()
    private var bunnDialogOppskriftLiveData = MutableLiveData<Meal>()
    private var soktOppskriftLiveData = MutableLiveData<List<Meal>>()




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
       RetrofitInstance.api.hentPopulareRetter("Beef").enqueue(object : Callback<OppskriftFraKategoriListe>{
           override fun onResponse(call: Call<OppskriftFraKategoriListe>, response: Response<OppskriftFraKategoriListe>) {
               if(response.body() != null){
                   populareRetterLiveData.value = response.body()!!.meals
               }
           }

           override fun onFailure(call: Call<OppskriftFraKategoriListe>, t: Throwable) {
               Log.d("HjemFragment", t.message.toString())
           }
       })
    }

    fun hentKategorier() {
        RetrofitInstance.api.hentKategorier().enqueue(object : Callback<KategoriListe>{
            override fun onResponse(call: Call<KategoriListe>, response: Response<KategoriListe>) {
                response.body()?.let { kategoriListe ->  
                    kategorierLiveData.postValue(kategoriListe.categories)
                }
            }

            override fun onFailure(call: Call<KategoriListe>, t: Throwable) {
                Log.e("HjemViewModel", t.message.toString())
            }
        })
    }

    fun hentOppskriftMedId(id:String) {
        RetrofitInstance.api.hentOppskriftDetaljer(id).enqueue(object : Callback<OppskriftListe>{
            override fun onResponse(
                call: Call<OppskriftListe>,
                response: Response<OppskriftListe>
            ) {
                val oppskrift = response.body()?.meals?.first()
                oppskrift?.let{ oppskrift ->
                    bunnDialogOppskriftLiveData.postValue(oppskrift)
                }
            }

            override fun onFailure(call: Call<OppskriftListe>, t: Throwable) {
                Log.e("HjemViewModel",t.message.toString())
            }
        })
    }

    fun slettOppskrift(oppskrift:Meal){
        viewModelScope.launch {
            oppskriftDB.oppskriftDao().slettOppskrift(oppskrift)
        }
    }
    fun insertOppskrift(oppskrift: Meal) {
        viewModelScope.launch {
            oppskriftDB.oppskriftDao().upsert(oppskrift)
        }
    }

    fun observeTilfeldigOppskriftLiveData():LiveData<Meal>{
        return randomOppskriftLiveData
    }

    fun observePopulareRetterLiveData():LiveData<List<OppskriftFraKategori>>{
        return populareRetterLiveData
    }

    fun observeKategorierLiveData():LiveData<List<Kategori>>{
        return kategorierLiveData
    }

    fun observerFavorittOppskrifterLiveData():LiveData<List<Meal>>{
        return favorittOppskriftLiveData
    }
    fun observerBunnDialogOppskrift() : LiveData<Meal> = bunnDialogOppskriftLiveData
}