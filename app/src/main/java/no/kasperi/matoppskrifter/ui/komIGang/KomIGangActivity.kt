package no.kasperi.matoppskrifter.ui.komIGang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_kom_igang.*
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.abstraction.AbstractActivity
import no.kasperi.matoppskrifter.aktiviteter.MainActivity

class KomIGangActivity : AbstractActivity(R.layout.activity_kom_igang) {

    private lateinit var viewModel : KomIGangRepository

    override fun init() {
        viewModel = ViewModelProvider(this).get(KomIGangRepository::class.java)
    }

    override fun running() {
        komigang_viewpager.adapter = viewModel.adapter

        neste_btn.setOnClickListener{
            when(komigang_viewpager.currentItem){
                0 -> komigang_viewpager.currentItem = 1
                1 -> komigang_viewpager.currentItem = 2
                2 -> Unit
            }
        }

        komigang_btn.setOnClickListener{
            goToHomepage()
        }

        komigang_skip_btn.setOnClickListener{
            goToHomepage()
        }
    }

    override fun stopped() {}


    fun goToHomepage(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}