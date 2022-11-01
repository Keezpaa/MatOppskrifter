package no.kasperi.matoppskrifter.ui.loggInn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_logg_inn.*
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.abstraction.AbstractActivity
import no.kasperi.matoppskrifter.aktiviteter.MainActivity
import no.kasperi.matoppskrifter.ui.glemtPassord.GlemtPassordActivity

class LoggInnActivity : AbstractActivity(R.layout.activity_logg_inn) {

    private lateinit var viewModel: LoggInnViewModel

    override fun init() {
        viewModel = ViewModelProvider(this).get(LoggInnViewModel::class.java)
    }

    override fun running() {
        logginn_btn.setOnClickListener {
            viewModel.validateUserInput(logginn_email_input, logginn_passord_input)
        }

        glemt_pass_btn.setOnClickListener {
            startActivity(Intent(this, GlemtPassordActivity::class.java))
        }

        observeViewModel()
    }

    override fun stopped() {
        viewModel.loginSuccessful.removeObservers(this)
        viewModel.loginFailed.removeObservers(this)
    }


    private fun observeViewModel() {
        viewModel.loginSuccessful.observe(this, Observer {
            updateUI(it)
        })

        viewModel.loginFailed.observe(this, Observer {
            when (it) {
                true -> Toast.makeText(this, "Vennligst fyll inn riktig informasjon", Toast.LENGTH_LONG)
                    .show()
                else -> {}
            }
        })
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let { startActivity(Intent(this, MainActivity::class.java)) }
    }
}
