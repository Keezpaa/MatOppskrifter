package no.kasperi.matoppskrifter.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import no.kasperi.matoppskrifter.db.OppskriftDB
import no.kasperi.matoppskrifter.db.Repository
import no.kasperi.matoppskrifter.pojo.MealDB
import no.kasperi.matoppskrifter.pojo.MealDetail
import no.kasperi.matoppskrifter.pojo.TilfeldigOppskriftRespons
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* TAG = HjemViewModel */
class DetaljerViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableOppskriftDetaljer = MutableLiveData<List<MealDetail>>()
    private val mutableOppskriftBunnDialog = MutableLiveData<List<MealDetail>>()
    private var alleOppskrifter: LiveData<List<MealDB>>
    private var repository: Repository

    init {
        val oppskriftDao = OppskriftDB.hentInstance(application).oppskriftDao()
        repository = Repository(oppskriftDao)
        alleOppskrifter = repository.oppskriftListe
    }

    fun hentAlleLagredeOppskrifter() {
        viewModelScope.launch(Dispatchers.Main) {
        }
    }

    fun leggTilOppskrift(meal: MealDB) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.leggTilFavorittOppskrift(meal)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun slettOppskrift(meal:MealDB) = viewModelScope.launch(Dispatchers.IO) {
        repository.slettOppskrift(meal)
    }

    fun hentOppskriftEtterId(id: String) {
        RetrofitInstance.api.hentOppskriftDetaljer(id).enqueue(object : Callback<TilfeldigOppskriftRespons> {
            override fun onResponse(call: Call<TilfeldigOppskriftRespons>, response: Response<TilfeldigOppskriftRespons>) {
                mutableOppskriftDetaljer.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<TilfeldigOppskriftRespons>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun erOppskriftLagretIDatabasen(mealId: String): Boolean {
        var meal: MealDB? = null
        runBlocking(Dispatchers.IO) {
            meal = repository.hentOppskriftEtterId(mealId)
        }
        if (meal == null)
            return false
        return true

    }

    fun slettOppskriftEtterId(mealId:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.slettOppskriftEtterId(mealId)
        }
    }

    fun hentOppskriftEtterIdBunnDialog(id: String) {
        RetrofitInstance.api.hentOppskriftDetaljer(id).enqueue(object : Callback<TilfeldigOppskriftRespons> {
            override fun onResponse(call: Call<TilfeldigOppskriftRespons>, response: Response<TilfeldigOppskriftRespons>) {
                mutableOppskriftBunnDialog.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<TilfeldigOppskriftRespons>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun observerOppskriftDetaljer(): LiveData<List<MealDetail>> {
        return mutableOppskriftDetaljer
    }

    fun observerOppskriftBunnDialog(): LiveData<List<MealDetail>> {
        return mutableOppskriftBunnDialog
    }

    fun observerLagretOppskrift(): LiveData<List<MealDB>> {
        return alleOppskrifter
    }
}