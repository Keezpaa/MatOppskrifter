package no.kasperi.matoppskrifter.ui.registrer

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_registrer.*
import no.kasperi.matoppskrifter.ui.komIGang.KomIGangActivity
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.abstraction.AbstractActivity

class RegistrerActivity : AbstractActivity(R.layout.activity_registrer) {

    private lateinit var viewModel: RegistrerViewModel

    override fun init() {
        viewModel = ViewModelProvider(this)[RegistrerViewModel::class.java]
    }

    override fun running() {
        registrer_btn.setOnClickListener {
            viewModel.validateUserInput(
                registrer_brukernavn_input,
                registrer_email_input,
                registrer_pass_input,
                registrer_bekreft_pass_input
            )
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.registerSuccessful.observe(this, Observer {
            when (it) {
                true -> {
                    startActivity(Intent(this, KomIGangActivity::class.java))
                }
                false -> {
                    Toast.makeText(this, "Fyll inn gyldig brukerinformasjon", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun stopped() {
        viewModel.registerSuccessful.removeObservers(this)
    }
}