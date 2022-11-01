package no.kasperi.matoppskrifter.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_intro.*
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.abstraction.AbstractActivity
import no.kasperi.matoppskrifter.ui.loggInn.LoggInnActivity
import no.kasperi.matoppskrifter.ui.registrer.RegistrerActivity

class IntroActivity : AbstractActivity(R.layout.activity_intro) {

    private lateinit var viewModel: IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IntroViewModel::class.java)
    }

    override fun init() {}

    override fun onStart() {
        super.onStart()
        val currentUser = viewModel.auth.currentUser
    }

    override fun running() {
        intro_logginn_btn.setOnClickListener {
            startActivity(Intent(this, LoggInnActivity::class.java))
        }

        intro_registrer_btn.setOnClickListener {
            startActivity(Intent(this, RegistrerActivity::class.java))
        }
    }

    override fun stopped() {}



    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // You can't go back | Log in to proceed
    }
}
