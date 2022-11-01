package no.kasperi.matoppskrifter.ui.komIGang

import androidx.lifecycle.ViewModel
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.modeller.KomIGangElement
import no.kasperi.matoppskrifter.ui.komIGang.adaptere.KomIGangAdapter

class KomIGangRepository : ViewModel() {

    val adapter = KomIGangAdapter()

    init {
        createOnboardingItems()
    }

    fun createOnboardingItems() {
        adapter.submitList(
            listOf(
                KomIGangElement(
                    R.drawable.komigang_ikon1,
                    "Finn din neste oppskrift",
                    "Du kan nå finne din neste oppskrift ut i fra hva du vil ha. Utforsk mange forskjellige oppskrifter i appen MatOppskrifter!"
                ),
                KomIGangElement(
                    R.drawable.komigang_ikon2,
                    "Legg til oppskrifter som favoritt",
                    "Du kan nå lagre dine favorittoppskrifter. Du har tilgang til dine beste oppskrifter i favoritter siden."
                ),
                KomIGangElement(
                    R.drawable.komigang_ikon3,
                    "Oversikt over kategorier",
                    "Mye oppskrifter på en gang? Ta det med ro! Du har full oversikt over forskjellige type retter i kategorier siden."
                )
            )
        )
    }
}