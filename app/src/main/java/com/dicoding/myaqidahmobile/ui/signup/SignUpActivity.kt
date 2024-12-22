package com.dicoding.myaqidahmobile.ui.signup

import android.annotation.*
import android.app.*
import android.content.*
import android.graphics.drawable.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.core.content.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*
import com.dicoding.myaqidahmobile.ui.login.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuthHelper: FirebaseAuthHelper

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi FirebaseAuth dan FirebaseFirestore
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        firebaseAuthHelper = FirebaseAuthHelper(auth, db)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Registrasi"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@SignUpActivity))

            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@SignUpActivity,
                        R.color.purple2
                    )
                )
            )
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.editDateOfBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    binding.editDateOfBirth.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                }, year, month, day)
            datePickerDialog.show()
        }

        binding.btnRegistrasi.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = binding.editNameRegistrasi.text.toString().trim()
        val dateOfBirth = binding.editDateOfBirth.text.toString().trim()
        val gender = binding.spinnerGender.selectedItem.toString().trim()
        val noHp = binding.noHpValue.text.toString().trim()
        val email = binding.editEmailRegistrasi.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (name.isNotEmpty() && dateOfBirth.isNotEmpty() && gender.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && noHp.isNotEmpty()) {
            binding.progressBarSignup.visibility = View.VISIBLE

            val userData = UserData(name, dateOfBirth, noHp, gender, email)
            firebaseAuthHelper.signUp(email, password, userData,
                onSuccess = {
                    binding.progressBarSignup.visibility = View.GONE
                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                },
                onFailure = { exception ->
                    binding.progressBarSignup.visibility = View.GONE
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
        }
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