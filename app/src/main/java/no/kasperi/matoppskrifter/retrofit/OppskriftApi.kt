package no.kasperi.matoppskrifter.retrofit

import no.kasperi.matoppskrifter.pojo.KategoriListe
import no.kasperi.matoppskrifter.pojo.OppskriftFraKategoriListe
import no.kasperi.matoppskrifter.pojo.OppskriftListe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OppskriftApi {

    @GET("random.php")
    fun hentTilfeldigOppskrift():Call<OppskriftListe>

    @GET("lookup.php")
    fun hentOppskriftDetaljer(@Query("i") id:String) : Call<OppskriftListe>

    @GET("filter.php")
    fun hentPopulareRetter(@Query("c") kategoriNavn:String) : Call<OppskriftFraKategoriListe>

    @GET("categories.php")
    fun hentKategorier() : Call<KategoriListe>

    @GET("filter.php")
    fun hentOppskrftFraKategori(@Query("c") kategoriNavn: String) : Call<OppskriftFraKategoriListe>

    @GET("search.php")
    fun sokEtterOppskrift(@Query("s") searchQuery:String) : Call<OppskriftListe>
}