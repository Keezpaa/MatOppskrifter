package no.kasperi.matoppskrifter.retrofit

import no.kasperi.matoppskrifter.pojo.OppskriftListe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OppskriftApi {

    @GET("random.php")
    fun getRandomOppskrift():Call<OppskriftListe>

    @GET("lookup.php?")
    fun getOppskriftDetaljer(@Query("i") id:String) :Call<OppskriftListe>
}