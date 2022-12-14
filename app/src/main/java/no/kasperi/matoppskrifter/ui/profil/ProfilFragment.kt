package no.kasperi.matoppskrifter.ui.profil

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_profil.*
import kotlinx.android.synthetic.main.last_opp_profilbilde_valg.view.*
import no.kasperi.matoppskrifter.R
import no.kasperi.matoppskrifter.abstraction.AbstractFragment
import no.kasperi.matoppskrifter.adapters.FavorittOppskriftAdapter
import no.kasperi.matoppskrifter.adapters.OppskriftAdapter
import no.kasperi.matoppskrifter.aktiviteter.OppskriftDetaljerActivity
import no.kasperi.matoppskrifter.databinding.FragmentProfilBinding
import no.kasperi.matoppskrifter.fragmenter.HjemFragment
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_BILDE
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_ID
import no.kasperi.matoppskrifter.fragmenter.HjemFragment.Companion.OPPSKRIFT_NAVN
import no.kasperi.matoppskrifter.pojo.MealDB
import no.kasperi.matoppskrifter.ui.redigerProfil.RedigerProfilActivity
import no.kasperi.matoppskrifter.viewModel.DetaljerViewModel
import no.kasperi.matoppskrifter.viewModel.HjemViewModel

class ProfilFragment: AbstractFragment(R.layout.fragment_profil) {
    lateinit var recView:RecyclerView
    private lateinit var binding:FragmentProfilBinding
    private lateinit var profilViewModel: ProfilViewModel
    private lateinit var detailsMVVM: DetaljerViewModel
    private lateinit var favorittAdapter: FavorittOppskriftAdapter
    private val REQUEST_IMAGE_CAPTURE = 100
    private val IMAGE_PICK_CODE = 200
    private val PERMISSION_CODE = 201


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favorittAdapter = FavorittOppskriftAdapter()
        detailsMVVM = ViewModelProvider(this)[DetaljerViewModel::class.java]

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView(view)
        onFavorittOppskriftClick()
        onFavorittLongOppskriftClick()

        detailsMVVM.observerLagretOppskrift().observe(viewLifecycleOwner,object : Observer<List<MealDB>>{
            override fun onChanged(t: List<MealDB>?) {
                favorittAdapter.setFavorittOppskriftListe(t!!)
                if(t.isEmpty())
                    binding.tvFavTom.visibility = View.VISIBLE

                else
                    binding.tvFavTom.visibility = View.GONE

            }
        })
    }



    override fun init(view: View) {
        profilViewModel = ViewModelProvider(this).get(ProfilViewModel::class.java)
    }

    override fun running() {
        profilViewModel.getUserPhoto()
        profilViewModel.getUserProfileData()

        profil_email.text = getString(R.string.profil_dummy_email)

        profil_bilde.setOnClickListener {
            showUploadPhotoOptions()
        }

        rediger_profil_btn.setOnClickListener {
            startActivity(Intent(requireContext(), RedigerProfilActivity::class.java))
        }

        profilViewModel.userEmail.observe(this, Observer {
            profil_email.text = it
        })

        profilViewModel.userImageLink.observe(this, Observer {
            Glide.with(this).load(it).into(profil_bilde)
        })

        profilViewModel.username.observe(this, Observer {
            profil_brukernavn.text = it
        })
    }



    override fun stop() {
        profilViewModel.userEmail.removeObservers(this)
        profilViewModel.userImageLink.removeObservers(this)
        profilViewModel.username.removeObservers(this)
    }

    private fun showUploadPhotoOptions() {
        val view = layoutInflater.inflate(R.layout.last_opp_profilbilde_valg, null)
        val dialog = BottomSheetDialog(view.context)
        dialog.setContentView(view)
        dialog.show()
        view.lastOppFraKamera.setOnClickListener {
            takePictureIntent()
            dialog.dismiss()
        }
        view.lastOppFraGalleri.setOnClickListener {
            if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                uploadFromGallery()
            }
            dialog.dismiss()
        }
    }


    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun uploadFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imgBitmap = data?.extras?.get("data") as Bitmap
            profilViewModel.saveCameraPhotoToDb(imgBitmap)

        } else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // I have to save the url to the db
            val imageUri = data?.data
            if (imageUri != null) {
                profilViewModel.saveGalleryPhotoToDb(imageUri)
                profilViewModel.getUserPhoto()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    uploadFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(context, "Ingen tilgang", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun prepareRecyclerView(v:View) {
        recView =v.findViewById<RecyclerView>(R.id.rec_favoritter_profil)
        recView.adapter = favorittAdapter
        recView.layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
    }


    private fun onFavorittOppskriftClick(){
        favorittAdapter.setOnFavorittOppskriftClickListener(object : FavorittOppskriftAdapter.OnFavorittClickListener{
            override fun onFavorittClick(meal: MealDB) {
                val intent = Intent(context, OppskriftDetaljerActivity::class.java)
                intent.putExtra(OPPSKRIFT_ID,meal.mealId.toString())
                intent.putExtra(OPPSKRIFT_NAVN,meal.mealName)
                intent.putExtra(OPPSKRIFT_BILDE,meal.mealThumb)
                startActivity(intent)
            }

        })
    }
    private fun onFavorittLongOppskriftClick() {
        favorittAdapter.setOnFavorittLongClickListener(object : FavorittOppskriftAdapter.OnFavorittLongClickListener{
            override fun onFavorittLongCLick(meal: MealDB) {
                detailsMVVM.hentOppskriftEtterIdBunnDialog(meal.mealId.toString())
            }

        })
    }
}