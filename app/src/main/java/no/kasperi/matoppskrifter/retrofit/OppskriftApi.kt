package no.kasperi.matoppskrifter.retrofit

import no.kasperi.matoppskrifter.pojo.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OppskriftApi {

    @GET("random.php")
    fun hentTilfeldigOppskrift():Call<TilfeldigOppskriftRespons>

    @GET("lookup.php")
    fun hentOppskriftDetaljer(@Query("i") id:String) : Call<TilfeldigOppskriftRespons>

    @GET("categories.php")
    fun hentKategorier() : Call<KategoriRespons>

    @GET("filter.php?")
    fun hentOppskriftEtterKategori(@Query("c") kategori:String):Call<OppskriftRespons>

    @GET("search.php?")
    fun sokEtterOppskrift(@Query("s") searchQuery:String) : Call<TilfeldigOppskriftRespons>
}