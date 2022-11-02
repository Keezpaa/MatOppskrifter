package no.kasperi.matoppskrifter.listeners

import no.kasperi.matoppskrifter.pojo.OppskriftFraKategori

interface OppskriftClickListener {
    fun onOppskriftClick(oppskrift: OppskriftFraKategori)
}