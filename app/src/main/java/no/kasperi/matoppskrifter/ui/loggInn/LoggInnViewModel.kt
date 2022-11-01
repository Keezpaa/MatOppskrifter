package no.kasperi.matoppskrifter.ui.loggInn

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoggInnViewModel : ViewModel() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val loginSuccessful = MutableLiveData<FirebaseUser?>()
    val loginFailed = MutableLiveData<Boolean>()


    fun validateUserInput(
        emailFelt: TextInputEditText,
        passordFelt: TextInputEditText
    ) {
        when (validateLoginCredentials(emailFelt, passordFelt)) {
            true -> {
                auth.signInWithEmailAndPassword(
                    emailFelt.text.toString(),
                    passordFelt.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            loginSuccessful.value = auth.currentUser
                            loginFailed.value = false
                        } else {
                            loginFailed.value = true
                            loginSuccessful.value = null
                        }
                    }
            }
            false -> Unit
        }
    }

    private fun validateLoginCredentials(
        emailField: TextInputEditText,
        passwordField: TextInputEditText
    ): Boolean {
        if (emailField.text.toString().isEmpty()) {
            emailField.error = "Fyll inn din e-mail adresse"
            emailField.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString()).matches()) {
            emailField.error = "Vennligst fyll inn en gyldig e-mail"
            emailField.requestFocus()
            return false
        }

        if (passwordField.text.toString().isEmpty()) {
            passwordField.error = "Fyll inn ditt passord"
            passwordField.requestFocus()
            return false
        }
        return true
    }
}