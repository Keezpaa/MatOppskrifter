package no.kasperi.matoppskrifter.db

import androidx.lifecycle.LiveData
import no.kasperi.matoppskrifter.pojo.MealDB


class Repository(private val mealDao: OppskriftDao) {

    val oppskriftListe: LiveData<List<MealDB>> = mealDao.hentAlleLagredeOppskrifter()

    suspend fun leggTilFavorittOppskrift(meal: MealDB) {
        mealDao.leggTilFavoritt(meal)
    }

    suspend fun hentOppskriftEtterId(mealId: String): MealDB {
        return mealDao.hentOppskriftEtterId(mealId)
    }

    suspend fun slettOppskriftEtterId(mealId: String) {
        mealDao.slettOppskriftEtterId(mealId)
    }

    suspend fun slettOppskrift(meal: MealDB) = mealDao.slettOppskrift(meal)


}

