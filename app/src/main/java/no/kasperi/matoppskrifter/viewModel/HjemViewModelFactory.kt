package no.kasperi.matoppskrifter.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import no.kasperi.matoppskrifter.db.OppskriftDB

class HjemViewModelFactory(
    val oppskriftDB : OppskriftDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HjemViewModel(oppskriftDB) as T
    }
}