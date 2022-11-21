package no.kasperi.matoppskrifter.ui.redigerProfil

import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_rediger_profil.*
import kotlinx.android.synthetic.main.oppdater_bruker_detaljer.view.*
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.abstraction.AbstractActivity
import no.kasperi.matoppskrifter.ui.intro.IntroActivity

class RedigerProfilActivity : AbstractActivity(R.layout.activity_rediger_profil) {

    private lateinit var viewModel: RedigerProfilViewModel
    private lateinit var oldUserEmail: String

    override fun init() {
        viewModel = ViewModelProvider(this).get(RedigerProfilViewModel::class.java)
    }

    override fun running() {
        viewModel.getUserData()

        rediger_brukernavn_btn.setOnClickListener {
            viewModel.laBrukernavnRedigeres()
        }

        rediger_email_btn.setOnClickListener {
            viewModel.laEmailRedigeres()
        }

        rediger_tlf_btn.setOnClickListener {
            viewModel.laTelefonRedigeres()
        }

        rediger_profil_lagre_btn.setOnClickListener{
            //save user input from all fields
            if (oldUserEmail != rediger_profil_email.text.toString()) {
                val newEmail = rediger_profil_email.text.toString()
                showLoginDialogue(newEmail)
                Toast.makeText(this,"Informasjon lagret!", Toast.LENGTH_SHORT).show()
            }

            viewModel.saveUsernameToDb(rediger_profil_brukernavn.text.toString())
            viewModel.savePhoneToDb(rediger_profil_tlf.text.toString())

        }

        rediger_profil_loggut_btn.setOnClickListener {
            askForLogout()
        }

        observeData()
    }

    override fun stopped() {
        viewModel.kanBrukerBliRedigert.removeObservers(this)
        viewModel.kanEmailBliRedigert.removeObservers(this)
        viewModel.kanTlfBliRedigert.removeObservers(this)
        viewModel.brukerEmail.removeObservers(this)
        viewModel.brukernavn.removeObservers(this)
        viewModel.telefonNummer.removeObservers(this)
    }

    private fun observeData() {
        viewModel.kanBrukerBliRedigert.observe(this, Observer {
            when (it) {
                true -> rediger_profil_brukernavn.isEnabled = true
                false -> rediger_profil_brukernavn.isEnabled = false
            }
        })

        viewModel.kanEmailBliRedigert.observe(this, Observer {
            when (it) {
                true -> rediger_profil_email.isEnabled = true
                false -> rediger_profil_email.isEnabled = false
            }
        })

        viewModel.kanTlfBliRedigert.observe(this, Observer {
            when (it) {
                true -> rediger_profil_tlf.isEnabled = true
                false -> rediger_profil_tlf.isEnabled = false
            }
        })

        viewModel.brukerEmail.observe(this, Observer {
            oldUserEmail = it
            it?.let { rediger_profil_email.setText(it) }
        })

        viewModel.brukernavn.observe(this, Observer {
            it?.let { rediger_profil_brukernavn.setText(it) }
        })

        viewModel.telefonNummer.observe(this, Observer {
            it?.let { rediger_profil_tlf.setText(it) }
        })
    }

    fun showLoginDialogue(newEmail: String) {
        val dialog =
            LayoutInflater.from(this).inflate(R.layout.oppdater_bruker_detaljer, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialog).show()

        dialog.rediger_profil_lagre_data_btn.setOnClickListener {
            val userEmail = dialog.bruker_email.text.toString()
            val userPassword = dialog.bruker_passord_felt.text.toString()
            viewModel.saveEmailToDb(userEmail, userPassword, newEmail)
            dialogBuilder.dismiss()
        }
    }

    private fun askForLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logg ut")
        builder.setMessage("Er du sikker på at du vil logge ut?")

        builder.setPositiveButton("Ja") { dialog, which ->
            viewModel.logoutUser()
            startActivity(Intent(this, IntroActivity::class.java))
        }

        builder.setNegativeButton("Avbryt") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

}