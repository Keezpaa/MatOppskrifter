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

    @GET("filter.php")
    fun hentPopulareRetter(@Query("c") kategoriNavn:String) : Call<OppskriftFraKategoriListe>

    @GET("categories.php")
    fun hentKategorier() : Call<KategoriListe>

    @GET("filter.php")
    fun hentOppskriftFraKategori(@Query("c") kategori: String) : Call<MealsResponse>

    @GET("search.php")
    fun sokEtterOppskrift(@Query("s") searchQuery:String) : Call<OppskriftListe>
}