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

class DetaljerViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableMealDetail = MutableLiveData<List<MealDetail>>()
    private val mutableMealBottomSheet = MutableLiveData<List<MealDetail>>()
    private  var allMeals: LiveData<List<MealDB>>
    private  var repository: Repository

    init {
        val mealDao = OppskriftDB.hentInstance(application).oppskriftDao()
        repository = Repository(mealDao)
        allMeals = repository.mealList
    }

    fun getAllSavedMeals() {
        viewModelScope.launch(Dispatchers.Main) {
        }
    }

    fun insertMeal(meal: MealDB) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoriteMeal(meal)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun deleteMeal(meal:MealDB) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMeal(meal)
    }

    fun getMealById(id: String) {
        RetrofitInstance.api.hentOppskriftDetaljer(id).enqueue(object : Callback<TilfeldigOppskriftRespons> {
            override fun onResponse(call: Call<TilfeldigOppskriftRespons>, response: Response<TilfeldigOppskriftRespons>) {
                mutableMealDetail.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<TilfeldigOppskriftRespons>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun isMealSavedInDatabase(mealId: String): Boolean {
        var meal: MealDB? = null
        runBlocking(Dispatchers.IO) {
            meal = repository.getMealById(mealId)
        }
        if (meal == null)
            return false
        return true

    }

    fun deleteMealById(mealId:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMealById(mealId)
        }
    }

    fun getMealByIdBottomSheet(id: String) {
        RetrofitInstance.api.hentOppskriftDetaljer(id).enqueue(object : Callback<TilfeldigOppskriftRespons> {
            override fun onResponse(call: Call<TilfeldigOppskriftRespons>, response: Response<TilfeldigOppskriftRespons>) {
                mutableMealBottomSheet.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<TilfeldigOppskriftRespons>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun observeMealDetail(): LiveData<List<MealDetail>> {
        return mutableMealDetail
    }

    fun observeMealBottomSheet(): LiveData<List<MealDetail>> {
        return mutableMealBottomSheet
    }

    fun observeSaveMeal(): LiveData<List<MealDB>> {
        return allMeals
    }
}