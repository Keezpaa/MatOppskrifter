package no.kasperi.matoppskrifter.db

import androidx.lifecycle.LiveData
import androidx.room.*
import no.kasperi.matoppskrifter.pojo.Meal

@Dao
interface OppskriftDao {


    // Oppdaterer og setter inn oppskrift = upsert (update og insert), med onConflict slipper man to funksjoner; insert og update.
    // 2 funksjoner i 1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(oppskrift: Meal)

    @Delete
    suspend fun slettOppskrift(oppskrift: Meal)

    @Query("SELECT * FROM oppskriftInfo")
    fun hentAlleOppskrifter():LiveData<List<Meal>>
}