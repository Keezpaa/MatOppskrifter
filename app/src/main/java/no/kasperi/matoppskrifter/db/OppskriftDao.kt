package no.kasperi.matoppskrifter.db

import androidx.lifecycle.LiveData
import androidx.room.*
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.MealDB

@Dao
interface OppskriftDao {

    @Insert
    fun leggTilFavoritt(meal: MealDB)

    @Update
    fun oppdaterFavoritt(meal:MealDB)

    @Query("SELECT * FROM oppskrift_informasjon order by mealId asc")
    fun hentAlleLagredeOppskrifter():LiveData<List<MealDB>>

    @Query("SELECT * FROM oppskrift_informasjon WHERE mealId =:id")
    fun hentOppskriftEtterId(id:String):MealDB

    @Query("DELETE FROM oppskrift_informasjon WHERE mealId =:id")
    fun slettOppskriftEtterId(id:String)

    @Delete
    fun slettOppskrift(meal:MealDB)

}