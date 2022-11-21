package no.kasperi.matoppskrifter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kasperi.matoppskrifter.pojo.*
import no.kasperi.matoppskrifter.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "HjemViewModel"

class HjemViewModel:ViewModel() {

    private val mutableKategori = MutableLiveData<KategoriRespons>()
    private val mutableTilfeldigOppskrift = MutableLiveData<TilfeldigOppskriftRespons>()
    private val mutableOppskriftEtterKategori = MutableLiveData<OppskriftRespons>()


    init {
        hentTilfeldigOppskrift()
        hentAlleKategorier()
        hentOppskriftEtterKategori()
    }


    private fun hentAlleKategorier() {
        RetrofitInstance.api.hentKategorier().enqueue(object : Callback<KategoriRespons> {
            override fun onResponse(call: Call<KategoriRespons>, response: Response<KategoriRespons>) {
                mutableKategori.value = response.body()
            }

            override fun onFailure(call: Call<KategoriRespons>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    private fun hentTilfeldigOppskrift() {
        RetrofitInstance.api.hentTilfeldigOppskrift().enqueue(object : Callback<TilfeldigOppskriftRespons> {
            override fun onResponse(call: Call<TilfeldigOppskriftRespons>, response: Response<TilfeldigOppskriftRespons>) {
                mutableTilfeldigOppskrift.value = response.body()
            }

            override fun onFailure(call: Call<TilfeldigOppskriftRespons>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }



    private fun hentOppskriftEtterKategori() {
        RetrofitInstance.api.hentOppskriftEtterKategori("chicken").enqueue(object : Callback<OppskriftRespons> {
            override fun onResponse(call: Call<OppskriftRespons>, response: Response<OppskriftRespons>) {
                mutableOppskriftEtterKategori.value = response.body()
            }

            override fun onFailure(call: Call<OppskriftRespons>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun observerOppskriftEtterKategori(): LiveData<OppskriftRespons> {
        return mutableOppskriftEtterKategori
    }

    fun observerTilfeldigOppskrift(): LiveData<TilfeldigOppskriftRespons> {
        return mutableTilfeldigOppskrift
    }

    fun observerKategorier(): LiveData<KategoriRespons> {
        return mutableKategori
    }

}