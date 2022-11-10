package no.kasperi.matoppskrifter.db

import androidx.lifecycle.LiveData
import androidx.room.*
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.MealDB

@Dao
interface OppskriftDao {


    @Insert
    fun insertFavorite(meal: MealDB)

    @Update
    fun updateFavorite(meal:MealDB)

    @Query("SELECT * FROM meal_information order by mealId asc")
    fun getAllSavedMeals():LiveData<List<MealDB>>

    @Query("SELECT * FROM meal_information WHERE mealId =:id")
    fun getMealById(id:String):MealDB

    @Query("DELETE FROM meal_information WHERE mealId =:id")
    fun deleteMealById(id:String)

    @Delete
    fun deleteMeal(meal:MealDB)




}