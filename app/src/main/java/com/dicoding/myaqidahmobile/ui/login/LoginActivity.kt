package com.dicoding.myaqidahmobile.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.databinding.ActivityLoginBinding
import com.dicoding.myaqidahmobile.ui.forgotpassword.ForgotPasswordActivity
import com.dicoding.myaqidahmobile.ui.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuthHelper: FirebaseAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth and Firestore, and FirebaseAuthHelper
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        firebaseAuthHelper = FirebaseAuthHelper(auth, db)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple3)
        supportActionBar?.hide()

        binding.registerButtonText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            binding.progressBarLogin.visibility = View.VISIBLE
            firebaseAuthHelper.login(
                email, password,
                onSuccess = {
                    binding.progressBarLogin.visibility = View.GONE
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    // Redirect to home screen or main activity after login success
                     startActivity(Intent(this, MainActivity::class.java))
                     finish()
                },
                onFailure = { exception ->
                    binding.progressBarLogin.visibility = View.GONE
                    Toast.makeText(this, "Login gagal: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            Toast.makeText(this, "Harap isi kedua kolom tersebut", Toast.LENGTH_SHORT).show()
        }
    }
}
