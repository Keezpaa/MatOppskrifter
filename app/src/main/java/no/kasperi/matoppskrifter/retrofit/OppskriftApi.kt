package no.kasperi.matoppskrifter.retrofit

import no.kasperi.matoppskrifter.pojo.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OppskriftApi {

    @GET("random.php")
    fun hentTilfeldigOppskrift():Call<RandomMealResponse>

    @GET("lookup.php")
    fun hentOppskriftDetaljer(@Query("i") id:String) : Call<RandomMealResponse>

    @GET("categories.php")
    fun hentKategorier() : Call<CategoryResponse>

    @GET("filter.php?")
    fun getMealsByCategory(@Query("c") category:String):Call<MealsResponse>

    @GET("search.php?")
    fun sokEtterOppskrift(@Query("s") searchQuery:String) : Call<RandomMealResponse>
}