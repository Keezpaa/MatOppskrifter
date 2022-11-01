package no.kasperi.matoppskrifter.ui.glemtPassord

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_glemt_passord.*
import no.kasperi.matoppskrifter.ui.loggInn.LoggInnActivity
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.abstraction.AbstractActivity

class GlemtPassordActivity : AbstractActivity(R.layout.activity_glemt_passord) {

    private lateinit var viewModel : GlemtPassordViewHolder

    override fun init() {
        viewModel = ViewModelProvider(this).get(GlemtPassordViewHolder::class.java)
    }

    override fun running() {
        glemt_pass_btn.setOnClickListener{
            viewModel.verifyEmail(glemt_pass_email.text.toString())
        }

        viewModel.userVerified.observe(this, Observer {
            when (it) {
                true -> showSuccessDialog()
                false -> Toast.makeText(this, "Ingen bruker funnet", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun stopped() {
        viewModel.userVerified.removeObservers(this)
    }


    private fun showSuccessDialog() {
        Snackbar.make(findViewById(android.R.id.content), "En e-post har blitt sendt til din e-postadresse", Snackbar.LENGTH_LONG)
            .show()

        startActivity(Intent(this, LoggInnActivity::class.java))
    }
}