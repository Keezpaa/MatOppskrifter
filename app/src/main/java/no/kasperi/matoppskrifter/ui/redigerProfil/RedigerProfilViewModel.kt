package no.kasperi.matoppskrifter.ui.redigerProfil

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RedigerProfilViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val database by lazy { FirebaseDatabase.getInstance().reference }
    val kanBrukerBliRedigert = MutableLiveData<Boolean>()
    val kanEmailBliRedigert = MutableLiveData<Boolean>()
    val kanTlfBliRedigert = MutableLiveData<Boolean>()
    val brukerEmail = MutableLiveData<String>()
    val brukernavn = MutableLiveData<String?>()
    val telefonNummer = MutableLiveData<String?>()
    private var brukernavnRedigering = false
    private var emailRedigering = false
    private var telefonRedigering = false

    fun getUserData() {
        brukerEmail.value = FirebaseAuth.getInstance().currentUser?.email.toString()
        getUserName()
        getUserPhoneNo()
    }

    fun laBrukernavnRedigeres() {
        brukernavnRedigering = !brukernavnRedigering
        kanBrukerBliRedigert.value = brukernavnRedigering
    }

    fun laEmailRedigeres() {
        emailRedigering = !emailRedigering
        kanEmailBliRedigert.value = emailRedigering
    }

    fun laTelefonRedigeres() {
        telefonRedigering = !telefonRedigering
        kanTlfBliRedigert.value = telefonRedigering
    }

    private fun getUserPhoneNo() {
        database.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userPhoneNo")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    when (p0.exists()) {
                        true -> telefonNummer.value = p0.value.toString()
                        false -> telefonNummer.value = "Ikke lagt til ennå"
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    telefonNummer.value = null
                }
            })
    }

    private fun getUserName() {
        database.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userData")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    when (p0.exists()) {
                        true -> brukernavn.value = p0.value.toString()
                        false -> Unit
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    brukernavn.value = null
                }
            })
    }

    fun savePhoneToDb(phoneNumber: String) {
        database.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userPhoneNo").setValue(phoneNumber)
        getUserData()
    }

    fun saveUsernameToDb(username: String) {
        database.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userData").setValue(username)
        getUserData()
    }

    fun saveEmailToDb(oldEmail: String, userPassword: String, newEmail: String) {
        auth.signInWithEmailAndPassword(oldEmail, userPassword)
            .addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    auth.currentUser!!.updateEmail(newEmail).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            brukerEmail.value = newEmail
                        }
                    }
                }
            }
    }

    fun logoutUser() {
        auth.signOut()
    }
}