package no.kasperi.matoppskrifter.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)