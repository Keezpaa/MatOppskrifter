package no.kasperi.matoppskrifter.ui.intro

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class IntroViewModel : ViewModel() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
}