package com.dicoding.myaqidahmobile.ui.profile.profilepasien

import android.Manifest
import android.content.pm.*
import android.graphics.drawable.*
import android.net.*
import android.os.*
import android.view.*
import androidx.activity.result.contract.*
import androidx.appcompat.app.*
import androidx.core.content.*
import com.bumptech.glide.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*
import com.google.android.material.snackbar.*
import com.google.firebase.auth.*
import com.google.firebase.storage.*

class ProfilePasienActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePasienBinding
    private var loadingDialog: AlertDialog? = null

    private lateinit var firebaseDataManager: FirebaseDataManager
    private val firebaseStorage by lazy { FirebaseStorage.getInstance() }

    private var selectedImageUri: Uri? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pickImageFromGallery()
        } else {
            showSnackbar("Permission denied to access gallery.")
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.profileImage.setImageURI(it)
            saveProfileImageToFirebase()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePasienBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDataManager = FirebaseDataManager()

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        showLoadingDialog()

        setupActionBar()

        fetchUser()

        binding.cameraIcon.setOnClickListener {
            checkGalleryPermissionAndPickImage()
        }
    }

    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setView(R.layout.loading_dialog)
            dialogBuilder.setCancelable(false)
            loadingDialog = dialogBuilder.create()
        }
        loadingDialog?.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Profile Pasien"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@ProfilePasienActivity))
            setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this@ProfilePasienActivity, R.color.purple2))
            )
        }
    }

    private fun fetchUser() {
        showLoadingDialog()

        FirebaseAuth.getInstance().currentUser?.let { user ->
            firebaseDataManager.getCurrentUser(
                userId = user.uid,
                onSuccess = { name, dateOfBirth, gender, noHp, email, profileImageUrl ->
                    hideLoadingDialog()

                    binding.patientName.text = name
                    binding.birthdateValue.text = dateOfBirth
                    binding.genderValue.text = gender
                    binding.noHpValue.text = noHp
                    binding.emailValue.text = email

                    if (profileImageUrl.isNotEmpty()) {
                        Glide.with(this)
                            .load(profileImageUrl)
                            .circleCrop()
                            .into(binding.profileImage)
                    }
                }
            )
        } ?: run {
            hideLoadingDialog()
            showSnackbar("Pengguna tidak ditemukan.")
        }
    }

    private fun checkGalleryPermissionAndPickImage() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                pickImageFromGallery()
            }

            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun saveProfileImageToFirebase() {
        showLoadingDialog()

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        selectedImageUri?.let { uri ->
            val storageRef = firebaseStorage.reference.child("profile_images/$userId.jpg")
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        saveImageUrlToDatabase(downloadUri.toString())
                    }.addOnFailureListener {
                        hideLoadingDialog()
                        showSnackbar("Failed to retrieve image URL.")
                    }
                }
                .addOnFailureListener {
                    hideLoadingDialog()
                    showSnackbar("Failed to upload profile image.")
                }
        }
    }

    private fun saveImageUrlToDatabase(imageUrl: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = firebaseDataManager.getUserReference(userId)
        userRef.update("profileImageUrl", imageUrl)
            .addOnSuccessListener {
                hideLoadingDialog()
                showSnackbar("Berhasil mengunggah")
                binding.profileImage.setImageURI(selectedImageUri)
            }
            .addOnFailureListener {
                hideLoadingDialog()
                showSnackbar("Failed to save image URL to database.")
            }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
