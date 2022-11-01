package no.kasperi.matoppskrifter.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.ui.intro.IntroActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        goToMain()
    }

    private fun goToMain() {
        Thread.sleep(2000)
        val i = Intent(this@SplashActivity, IntroActivity::class.java)
        finish()
        startActivity(i)
    }
}