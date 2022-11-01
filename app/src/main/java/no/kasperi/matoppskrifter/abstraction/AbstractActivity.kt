package no.kasperi.matoppskrifter.abstraction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class AbstractActivity(contentLayoutId : Int) : AppCompatActivity(contentLayoutId) {

    abstract fun init()
    abstract fun running()
    abstract fun stopped()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onPostResume() {
        super.onPostResume()
        running()
    }

    override fun onStop() {
        stopped()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}