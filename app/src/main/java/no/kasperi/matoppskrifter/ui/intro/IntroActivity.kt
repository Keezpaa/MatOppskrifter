package no.kasperi.matoppskrifter.ui.intro

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.BoringLayout.make
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_intro.*
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.abstraction.AbstractActivity
import no.kasperi.matoppskrifter.databinding.ActivityIntroBinding
import no.kasperi.matoppskrifter.databinding.ActivityMainBinding
import no.kasperi.matoppskrifter.ui.loggInn.LoggInnActivity
import no.kasperi.matoppskrifter.ui.registrer.RegistrerActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import no.kasperi.matoppskrifter.aktiviteter.MainActivity

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
        updateUI(currentUser)

    }

    override fun running() {
        intro_logginn_btn.setOnClickListener {
            startActivity(Intent(this, LoggInnActivity::class.java))
        }

        intro_registrer_btn.setOnClickListener {
            startActivity(Intent(this, RegistrerActivity::class.java))
        }
    }
    private fun updateUI(user: FirebaseUser?) {
        user?.let { startActivity(Intent(this, MainActivity::class.java)) }
    }
    override fun stopped() {}
}
