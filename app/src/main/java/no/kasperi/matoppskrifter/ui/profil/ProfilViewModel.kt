package no.kasperi.matoppskrifter.ui.profil

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.kasperi.matoppskrifter.pojo.Meal
import no.kasperi.matoppskrifter.pojo.MealDB
import no.kasperi.matoppskrifter.pojo.MealDetail

class ProfilViewModel : ViewModel(){
    private val repo = ProfilRepository()
    private lateinit var allMeals: LiveData<List<MealDB>>
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
