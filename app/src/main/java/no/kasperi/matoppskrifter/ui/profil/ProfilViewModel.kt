package no.kasperi.matoppskrifter.ui.profil

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilViewModel : ViewModel(){
    private val repo = ProfilRepository()
    val userEmail = repo.userEmail
    val userImageLink = repo.userImageLink
    val username = repo.username





    fun saveCameraPhotoToDb(imgBitmap: Bitmap) {
        repo.saveCameraPhotoToDb(imgBitmap)
    }

    fun saveGalleryPhotoToDb(imageUri: Uri) {
        repo.saveGalleryPhotoToDb(imageUri)
    }

    fun getUserPhoto() {
        repo.getUserPhoto()
    }

    fun getUserProfileData() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.getUserProfileData()
        }
    }
}
